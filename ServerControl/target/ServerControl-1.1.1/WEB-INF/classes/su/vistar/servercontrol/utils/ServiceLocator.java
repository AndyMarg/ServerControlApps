package su.vistar.servercontrol.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import su.vistar.servercontrol.utils.settings.SettingsManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.servercomponents.CheckManager;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent;
import su.vistar.servercontrol.servercomponents.ServerComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.checkerpagewithstringvalues.YandexForwardComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues.CDSForwardComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues.M2MForwardComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.chekerpagewithdatavalues.VMJobsComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.gpsadapter.GPSAdapterComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.ShadowClientAdapterHeir;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.VIStarMarshrutComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.ShadowClient;
import su.vistar.servercontrol.servercomponents.checkers.various.vmdata.VMDataComponent;
import su.vistar.servercontrol.servercomponents.checkers.various.сhekerpagewithlongvalues.VPArrivalServerComponent;
import util.concurrent.SafeScheduledExecutorService;

public class ServiceLocator {

    private static final LoggerManager log = new LoggerManager();

    private static class Refs {

        private CurrentStateForComponent currentState;
        private ScheduledExecutorService scheduledExecutorService;
        private SettingsManager settingsManager;
        private CheckManager checkManager;
        private ShadowClientAdapterHeir adapterHeir;
        private ShadowClient shadowClient;

        private YandexForwardComponent yandexForwardComponent;
        private CDSForwardComponent cDSForwardComponent;
        private M2MForwardComponent m2MForwardComponent;
        private VMJobsComponent vMJobsComponent;
        private GPSAdapterComponent adapterComponent;
        private VMDataComponent dataComponent;
        private VPArrivalServerComponent arrivalServerComponent;
        private VIStarMarshrutComponent vIStarMarshrutComponent;

    }

    private static Refs refs = new Refs();

    public static void initialize() {
        refs.scheduledExecutorService = new SafeScheduledExecutorService(
                Executors.newScheduledThreadPool(2));

        refs.settingsManager = new SettingsManager();
        refs.settingsManager.initialize();
        refs.currentState = new CurrentStateForComponent();
        refs.checkManager = new CheckManager();
        refs.adapterHeir = new ShadowClientAdapterHeir();
        refs.shadowClient = new ShadowClient(
                refs.settingsManager.getShadowClientSettings().server_adress,
                refs.settingsManager.getShadowClientSettings().port,
                refs.settingsManager.getShadowClientSettings().key,
                refs.adapterHeir);

        refs.dataComponent = new VMDataComponent("VMData");
        refs.adapterComponent = new GPSAdapterComponent("GPSAdapter");
        refs.arrivalServerComponent = new VPArrivalServerComponent("VPArrival");
        refs.cDSForwardComponent = new CDSForwardComponent("CDSForward");
        refs.m2MForwardComponent = new M2MForwardComponent("M2MForward");
        refs.yandexForwardComponent = new YandexForwardComponent("YandexForward");
        refs.vMJobsComponent = new VMJobsComponent("VMJobs");
        refs.vIStarMarshrutComponent = new VIStarMarshrutComponent("VIStarMarshrut");

        // инициализация компонентов
        for (ServerComponent component : getComponents()) {
            component.initialize();
        }
    }

    public static void shutdown() {
        refs.scheduledExecutorService.shutdownNow();

        // деинициализация компонентов
        for (ServerComponent component : getComponents()) {
            component.shutdown();
        }

        // cleanup
        refs = null;
    }

    public static CurrentStateForComponent getCurrentStateObject() {
        return refs.currentState;
    }

    public static ScheduledExecutorService getScheduledExecutorService() {
        return refs.scheduledExecutorService;
    }

    public static SettingsManager getSettingsManager() {
        return refs.settingsManager;
    }

    public static CheckManager getCheckManager() {
        return refs.checkManager;
    }

    public static ShadowClientAdapterHeir getShadowClientAdapterHeir() {
        return refs.adapterHeir;
    }

    public static ShadowClient getShadowClient() {
        return refs.shadowClient;
    }

    public static YandexForwardComponent getYandexForwardComponent() {
        return refs.yandexForwardComponent;
    }

    public static VPArrivalServerComponent getVPArrivalServerComponent() {
        return refs.arrivalServerComponent;
    }

    public static VMDataComponent getVMDataComponent() {
        return refs.dataComponent;
    }

    public static VMJobsComponent getVMJobsComponent() {
        return refs.vMJobsComponent;
    }

    public static CDSForwardComponent getCDSForwardComponent() {
        return refs.cDSForwardComponent;
    }

    public static M2MForwardComponent getM2MForwardComponent() {
        return refs.m2MForwardComponent;
    }

    public static GPSAdapterComponent getGPSAdapterComponent() {
        return refs.adapterComponent;
    }

    public static VIStarMarshrutComponent getVIStarMarshrutComponent() {
        return refs.vIStarMarshrutComponent;
    }

    private static List<ServerComponent> getComponents() {
        ArrayList<ServerComponent> components = new ArrayList<>();
        for (Field field : refs.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(refs);
                if (obj instanceof ServerComponent) {
                    components.add((ServerComponent) obj);
                }
            } catch (Exception ex) {
                log.severe(ex, "Ошибка при получении списка компонентов");
            }
        }
        return components;
    }
}
