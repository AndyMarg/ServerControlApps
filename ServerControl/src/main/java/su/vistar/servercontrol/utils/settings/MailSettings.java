package su.vistar.servercontrol.utils.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MailSettings {

    public final String password;
    public final String login;
    public final List<String> adress_to = new ArrayList<>();
    public final String adress_from;
    public final int smtpPort;
    public final String stmpHost;
    String to = null;

//====================================================================================
    //Настройки по умолчанию для добаления в БД
    static final String[][] DEFAULT_VALUES = {
        {"password", "hysteria52"},
        {"login", "alisa.margashova@vistar.su"},
        {"adress_to", "techerror@vistar.su,alisa.margashova@vistar.su"},
        {"adress_from", "alisa.margashova@vistar.su"},
        {"smtpPort", "587"},
        {"stmpHost", "smtp.yandex.ru"}
    };
    //Имя компонента для добавления в БД
    static final String COMPONENT_NAME = "mail";

//====================================================================================
    /**
     * Конструктор класса MailSettings,используемый для получения настроек для
     * почты.
     *
     * @param values набор настроек для почты,полченных из БД
     */
    public MailSettings(Map<String, String> values) {
        password = SettingsUtil.getStringValue(values, "password");
        login = SettingsUtil.getStringValue(values, "login");

        to = SettingsUtil.getStringValue(values, "adress_to");
        //Получение массива адресатов
        String adresses[] = to.split(",");
        for (String adress : adresses) {
            adress.replaceAll(" ", "");
            adress_to.add(adress);
        }

        adress_from = SettingsUtil.getStringValue(values, "adress_from");
        smtpPort = Integer.parseInt(SettingsUtil.getStringValue(values, "smtpPort"));
        stmpHost = SettingsUtil.getStringValue(values, "stmpHost");
    }

}
