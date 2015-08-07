package su.vistar.servercontrol.utils.settings;

import java.util.Map;

class SettingsUtil {
/**
 * Получает значение настройки компонента. 
 * 
 * @param values набор настроек
 * @param name имя настройки
 * @return  значение настройки
 */
    
    public static String getStringValue(Map<String, String> values, String name) {
        String value = values.get(name);
        if (value == null) {
            throw new RuntimeException("Нет необходимого значения настройки " + name);
        }
        return value;
    }
    
}
