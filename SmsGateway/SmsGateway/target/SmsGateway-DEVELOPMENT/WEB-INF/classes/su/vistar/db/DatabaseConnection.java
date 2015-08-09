package su.vistar.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import su.vistar.db.mapper.MessageMapper;
import su.vistar.logging.LoggerManager;

public class DatabaseConnection {

    private static volatile SqlSessionFactory database = null;
    public static LoggerManager log = new LoggerManager();

    public static void initialize() {
        //Получение файла с настройками для mbatis
        String resource = "su/vistar/db/SqlConfigMessages.xml";
        try {
            //Настройка конфиграции
            Reader reader = Resources.getResourceAsReader(resource);
            database = new SqlSessionFactoryBuilder().build(reader);
            Configuration config = database.getConfiguration();
            //Добавление мапперов
            config.addMappers(MessageMapper.class.getPackage().getName());
        } catch (FileNotFoundException e) {
            log.severe(e, "Can't find file " + resource);
        } catch (IOException e) {
            log.severe(e);
        }
    }

    public static void cleanup() {
        database = null;
    }

    public static SqlSessionFactory getDatabase() {
        if (database == null) {
            throw new IllegalStateException("DatabaseConnection doesn't initialize");
        }
        return database;
    }
}
