package com.rahimunisha.rahimunishamart.listener;

import com.rahimunisha.rahimunishamart.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * AppContextListener: Manages the lifecycle of HikariCP Connection Pool.
 * Satisfies Week 1 Standing Rule: single ServletContextListener owning HikariCP lifecycle.
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing RahimunishaMart Application Context...");
        try {
            DatabaseUtil.initDataSource();
            logger.info("Application context and HikariCP connection pool initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize application context: {}", e.getMessage(), e);
            throw new RuntimeException("Context initialization failure", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Destroying RahimunishaMart Application Context...");
        DatabaseUtil.closeDataSource();
        logger.info("Application context destroyed.");
    }
}
