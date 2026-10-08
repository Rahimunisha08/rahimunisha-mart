package com.rahimunisha.rahimunishamart.server;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * EmbeddedServer: Standalone Tomcat 9 runner allowing 1-click execution.
 * Also packages into standard .WAR for deployment on external Tomcat servers (Docker, PaaS, VM).
 */
public class EmbeddedServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portEnv);
            } catch (NumberFormatException ignored) {
            }
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Initialize default HTTP connector

        String webappDirLocation = "src/main/webapp";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
            webappDirLocation = ".";
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        ctx.setReloadable(true);

        // Declare alternative location for your "WEB-INF/classes" dir
        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        System.out.println("==================================================================");
        System.out.println("   RAHIMUNISHA MART CAPSTONE SERVER INITIALIZING");
        System.out.println("   Access Web Application at: http://localhost:" + port);
        System.out.println("   Health Status Endpoint at: http://localhost:" + port + "/api/v1/health");
        System.out.println("   Press Ctrl+C to shut down.");
        System.out.println("==================================================================");

        tomcat.start();
        tomcat.getServer().await();
    }
}
