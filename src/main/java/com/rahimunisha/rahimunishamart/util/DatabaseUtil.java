package com.rahimunisha.rahimunishamart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * DatabaseUtil: Singleton managing HikariCP Connection Pool lifecycle.
 * Governed exclusively by AppContextListener.
 * Enforces:
 * - NO DriverManager.getConnection() calls
 * - HikariDataSource connection pooling
 * - PreparedStatement execution
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static volatile HikariDataSource dataSource;

    private DatabaseUtil() {
    }

    public static synchronized void initDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        try {
            // Ensure data directory exists if using relative file path
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            HikariConfig config = new HikariConfig();
            String driver = ConfigUtil.get("db.driver", "org.h2.Driver");
            String url = ConfigUtil.get("db.url", "jdbc:h2:file:./data/rahimunishamart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;MODE=LEGACY");
            String username = ConfigUtil.get("db.username", "sa");
            String password = ConfigUtil.get("db.password", "");

            int maxPoolSize = ConfigUtil.getInt("db.pool.max.size", 10);
            int minIdle = ConfigUtil.getInt("db.pool.min.idle", 2);
            long timeout = ConfigUtil.getInt("db.pool.timeout", 30000);

            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);

            config.setMaximumPoolSize(maxPoolSize);
            config.setMinimumIdle(minIdle);
            config.setConnectionTimeout(timeout);
            config.setPoolName("RahimunishaMartPool");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP connection pool initialized successfully with URL: {}", url);

            // Execute schema and seed data if not yet present
            runSchemaAndMigrations();
        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP connection pool", e);
            throw new RuntimeException("Database pool initialization failed", e);
        }
    }

    public static synchronized void initDataSource(HikariDataSource customDataSource) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        dataSource = customDataSource;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            synchronized (DatabaseUtil.class) {
                if (dataSource == null || dataSource.isClosed()) {
                    initDataSource();
                }
            }
        }
        return dataSource.getConnection();
    }

    public static synchronized void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Closing HikariCP connection pool...");
            dataSource.close();
            dataSource = null;
            logger.info("HikariCP connection pool closed.");
        }
    }

    public static boolean isHealthy() {
        if (dataSource == null || dataSource.isClosed()) {
            return false;
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1");
             ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        } catch (Exception e) {
            logger.warn("Database health check failed: {}", e.getMessage());
            return false;
        }
    }

    public static void runSchemaAndMigrations() {
        try (Connection conn = getConnection()) {
            logger.info("Initializing schema.sql (tables with IF NOT EXISTS)...");
            executeSqlScript(conn, "schema.sql");

            logger.info("Executing migrations: v2__order_status.sql...");
            executeSqlScript(conn, "migrations/v2__order_status.sql");

            // Check if users table is empty
            boolean needsSeed = false;
            try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        needsSeed = true;
                    }
                }
            } catch (SQLException e) {
                needsSeed = true;
            }

            if (needsSeed) {
                logger.info("Populating database with seed.sql demo data...");
                executeSqlScript(conn, "seed.sql");
                logger.info("Database initialized and seeded successfully.");
            } else {
                logger.info("Existing database data detected. Syncing seed credentials...");
                try (PreparedStatement fixPw = conn.prepareStatement(
                        "UPDATE users SET password_hash = ? WHERE email IN ('admin@rahimunishamart.com', 'techseller@rahimunishamart.com', 'styleseller@rahimunishamart.com', 'nisha@rahimunishamart.com', 'demo.buyer@rahimunishamart.com')")) {
                    fixPw.setString(1, "$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa");
                    fixPw.executeUpdate();
                }
            }
        } catch (Exception e) {
            logger.error("Error executing database schema / seed scripts", e);
        }
    }

    public static void executeSqlScript(Connection conn, String scriptPath) throws Exception {
        try (InputStream in = DatabaseUtil.class.getClassLoader().getResourceAsStream(scriptPath)) {
            if (in == null) {
                logger.warn("SQL script not found in classpath: {}", scriptPath);
                return;
            }
            org.h2.tools.RunScript.execute(conn, new InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
        }
    }
}
