package su.vistar.util;

import su.vistar.db.CommonFunctions;

public class ServiceLocator {

    private static class Refs {

        private CommonFunctions commonFunctions;
    }

    private static Refs refs = new Refs();

    public static void initialize() {
        refs.commonFunctions = new CommonFunctions();
    }

    public static void shutdown() {
        // cleanup
        refs = null;
    }
    
    public static CommonFunctions getCommonFunction(){
        return refs.commonFunctions;
    }

}
