package su.vistar.servercontrol.utils.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.ibatis.session.SqlSession;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.db.DatabaseConnection;
import su.vistar.servercontrol.entity.ComponentEntity;
import su.vistar.servercontrol.db.mapper.MapperMysql;
import su.vistar.servercontrol.utils.ServiceLocator;

public class SettingsManager {
    
    public static final LoggerManager log = new LoggerManager();
    private MailSettings mailSettings;
    private ShadowClientSettings shadowClientSettings;

//==========================================================================================
    //Получение настроек для отправки сообщения на почту
    public MailSettings getMailSettings() {
        return mailSettings;
    }

    public ShadowClientSettings getShadowClientSettings() {
        return shadowClientSettings;
    }

    // Инициализация получения настроек для отправки сообщения из базы данных
    public void initialize() {
        loadSettings();
        
        // запускаем обновление по таймеру
        ServiceLocator.getScheduledExecutorService().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                //Получение настроек
                loadSettings();
            }
        }, 0, 3, TimeUnit.MINUTES);
    }

    //=======================================================================================    
    
    private void loadSettings() {
        loadMailSettings();
        loadShadowClientSettings();
    }
    
    /**
     * Получает настройки из базы данных.
     *
     * @return объект HashMap с именем настройки(в виде ключа) и значением
     */
    private void loadMailSettings() {
        Map<String, String> values = getSettingValues(MailSettings.COMPONENT_NAME);

        addDefaultValues(values, MailSettings.DEFAULT_VALUES, MailSettings.COMPONENT_NAME);
        this.mailSettings = new MailSettings(values);
    }

    private void loadShadowClientSettings() {
        Map<String, String> values = getSettingValues(ShadowClientSettings.COMPONENT_NAME);
        
        addDefaultValues(values, ShadowClientSettings.DEFAULT_VALUES, ShadowClientSettings.COMPONENT_NAME);
        this.shadowClientSettings = new ShadowClientSettings(values);
    }
    //=======================================================================================    

    /**
     * Добавление необходимых настроек почты в БД,если в БД их нет.
     *
     * @param settingsMap набор настроек
     * @param defaultValues настройки которые надо добавить в БД
     * @param componentName название компонента,настройки которого надо добавить
     * в БД
     */
    private void addDefaultValues(Map<String, String> settingsMap, String[][] defaultValues, String componentName) {

        MapperMysql mapp;

        //Открытие соединения с базой данных и получения маппера для считывания настроек отправки сообщения на почту
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            mapp = session.getMapper(MapperMysql.class);
            for (String[] strings : defaultValues) {
                String name = strings[0];
                String value = strings[1];
                //ПРоверка на наличие нужной настройки в заданном наборе
                //Если нет,то добавляем и в набор и в БД
                if (!settingsMap.containsKey(name)) {
                    settingsMap.put(name, value);
                    mapp.addComponentSettings(componentName, name, value);
                }
            }

            session.commit();
        }
    }

    private Map<String, String> getSettingValues(String componentName) {

        Map<String, String> values = new HashMap<>();
        //Открытие соединения с базой данных и получения маппера для считывания настроек отправки сообщения на почту
        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            MapperMysql mapp = session.getMapper(MapperMysql.class);

            // Получение настроек для почты
            List<ComponentEntity> settingEntities = mapp.selectComponentSettings(componentName);
            for (ComponentEntity en : settingEntities) {
                values.put(en.getName(), en.getValue());
            }

        }
        catch(Exception e){
            log.severe(e, "ОШИБКА!!!!!");
        }

        return values;
    }
}
