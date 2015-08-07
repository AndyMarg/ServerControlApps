package su.vistar.servercontrol.contextlistner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import su.vistar.servercontrol.db.DatabaseConnection;
import su.vistar.servercontrol.servercomponents.CheckManager;
import su.vistar.servercontrol.utils.ServiceLocator;

public class ContextListener implements ServletContextListener {

//=================================================================================================
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //Инициализация базы данных
        DatabaseConnection.initialize();
        //инициализация менеджера настроек,менеджера проверки
        ServiceLocator.initialize();

        CheckManager checkManager = ServiceLocator.getCheckManager();

        //Выполнение циклов проверки активности серверов
        checkManager.scheduleCheck(ServiceLocator.getYandexForwardComponent(), 1);
        checkManager.scheduleCheck(ServiceLocator.getVMJobsComponent(), 5);
        checkManager.scheduleCheck(ServiceLocator.getCDSForwardComponent(), 5);
        checkManager.scheduleCheck(ServiceLocator.getM2MForwardComponent(), 5);
        checkManager.scheduleCheck(ServiceLocator.getVPArrivalServerComponent(), 1);
        checkManager.scheduleCheck(ServiceLocator.getGPSAdapterComponent(), 5);
        checkManager.scheduleCheck(ServiceLocator.getVMDataComponent(), 5);
        checkManager.scheduleCheck(ServiceLocator.getVIStarMarshrutComponent(), 1);
    }
//===================================================================================================

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        ServiceLocator.shutdown();   
        DatabaseConnection.cleanup();
    }
}
