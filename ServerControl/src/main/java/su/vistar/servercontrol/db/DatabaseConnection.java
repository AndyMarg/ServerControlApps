package su.vistar.servercontrol.db;

import su.vistar.servercontrol.db.mapper.MapperMysql;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.misc.config.Config;



public class DatabaseConnection {
        private static volatile SqlSessionFactory database = null;
         private static final LoggerManager log = new LoggerManager();
         
  //==================================================================================
     
    //Инициализация базы данных
    public static void initialize() {

        //Получение конфигурационного файла MyBatis
        String resource = "su/vistar/servercontrol/db/SqlConfigTools.xml";
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            String environment = Config.getMybatisEnvironment();
            database = new SqlSessionFactoryBuilder().build(reader, environment);
            //Добавление мапперов
            Configuration config = database.getConfiguration();
            config.addMappers(MapperMysql.class.getPackage().getName());
        } catch (FileNotFoundException e) {
            log.severe(e,"Configuration file isn't found");
        } catch (IOException e) {
            log.severe(e,"IOException!");
        }
    }
    
//========================================================================================
    public static void cleanup() {
        database = null;
    }

//========================================================================================
    public static SqlSessionFactory getDatabase() {
        if (database == null) {
            throw new IllegalStateException("DatabaseConnection не инициализирован");
        }
        return database;
    }
}
