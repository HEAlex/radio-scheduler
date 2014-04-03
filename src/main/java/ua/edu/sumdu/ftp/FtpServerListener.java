package ua.edu.sumdu.ftp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ftpserver.FtpServer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class FtpServerListener implements ServletContextListener {

    public static final String FTPSERVER_CONTEXT_NAME = "org.apache.ftpserver";

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Stopping FtpServer");
        FtpServer server = (FtpServer) sce.getServletContext().getAttribute(FTPSERVER_CONTEXT_NAME);

        if(server != null) {
            server.stop();
            sce.getServletContext().removeAttribute(FTPSERVER_CONTEXT_NAME);
            System.out.println("FtpServer stopped");
        } else {
            System.out.println("No running FtpServer found");
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Starting FtpServer");
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        FtpServer server = (FtpServer) ctx.getBean("ftpServer");
        sce.getServletContext().setAttribute(FTPSERVER_CONTEXT_NAME, server);
        try {
            server.start();
            System.out.println("FtpServer started");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start FtpServer", e);
        }
    }
}