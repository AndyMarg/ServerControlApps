package su.vistar.servercontrol.misc.version;

import java.util.Properties;

public class Version {

    private static final String VERSION;
    private static final String BUILD_TIME;
    private static final String COMMIT_NUMBER;

//===================================================================================
    /**
     *
     * @return текущую версию проекта
     */
    public static String getVERSION() {
        return VERSION;
    }

//====================================================================================
    /**
     *
     * @return время сборки
     */
    public static String getBUILD_TIME() {
        return BUILD_TIME;
    }

//======================================================================================    
    /**
     *
     * @return коммит
     */
    public static String getCOMMIT_NUMBER() {
        return COMMIT_NUMBER;
    }

//======================================================================================
//Получает список свойств из файла Version.properties
    static {
        Properties props = new Properties();
        try {
            Class clazz = Version.class;

            props.load(clazz.getResourceAsStream("/"
                    + clazz.getPackage().getName().replace('.', '/') + "/Version.properties"));

        } catch (Throwable t) {
            throw new Error(t);
        }
//Устанавливает версию,время сборки,коммит
        VERSION = props.getProperty("VERSION");
        BUILD_TIME = props.getProperty("BUILD_TIME");
        COMMIT_NUMBER = props.getProperty("COMMIT_NUMBER");

    }

}
