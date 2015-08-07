package su.vistar.servercontrol.utils.settings;

import java.util.Map;

public class ShadowClientSettings {

    public final String server_adress;
    public final int port;
    public final int key;

//====================================================================================
    //Настройки по умолчанию для добаления в БД
    static final String[][] DEFAULT_VALUES = {
        {"server_adress", "marshrut.vistar.su"},
        {"port", "20997"},
        {"key", "0"}
    };
    //Имя компонента для добавления в БД
    static final String COMPONENT_NAME = "shadowClient";

//====================================================================================
    /**
     * Конструктор класса ShadowClientSettings,используемый для получения
     * настроек для почты.
     *
     * @param values набор настроек для почты,полченных из БД
     */
    public ShadowClientSettings(Map<String, String> values) {
        server_adress = SettingsUtil.getStringValue(values, "server_adress");
        port = Integer.parseInt(SettingsUtil.getStringValue(values, "port"));
        key = Integer.parseInt(SettingsUtil.getStringValue(values, "key"));
    }
}
