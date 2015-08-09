package su.vistar.contextlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import su.vistar.util.ServiceLocator;
import su.vistar.db.DatabaseConnection;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        DatabaseConnection.initialize();
        ServiceLocator.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConnection.cleanup();
    }

}
