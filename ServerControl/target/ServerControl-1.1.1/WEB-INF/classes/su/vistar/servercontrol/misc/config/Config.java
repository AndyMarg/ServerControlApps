package su.vistar.servercontrol.misc.config;

public class Config {

    //список окружений для работы с базой данных
    public static enum WorkMode {
        DEVELOPMENT,
        PRODUCTION
    }
    
    //Окружение по умолчанию   
    private static final WorkMode workMode = WorkMode.DEVELOPMENT;

    //==================================================================================   
    /**
     *
     * @return текущее окружение
     */
    public static WorkMode getWorkMode() {
        return workMode;
    }

    //===================================================================================    
    /**
     *
     * @return окружение для БД
     */
    public static String getMybatisEnvironment() {
        switch (workMode) {
            case DEVELOPMENT:
                return "development";

            case PRODUCTION:
                return "production";
        }

        throw new RuntimeException("Unsupported work mode: " + workMode);
    }

}
