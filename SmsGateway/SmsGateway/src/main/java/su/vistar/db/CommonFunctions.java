package su.vistar.db;

import java.util.Date;
import org.apache.ibatis.session.SqlSession;
import static su.vistar.action.SendMessage.log;
import su.vistar.db.mapper.MessageMapper;

public class CommonFunctions {
    
//=================================================================================
    
    /**
     * Добавляет в БД время отправки сообщения на устройство.
     * @param date время,добавляемое в базу данных
     * @param messageId id сообщения для которого добавляется время
     */
    public void setSendToPhoneTime(Date date, Integer messageId) {

        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                mapp.addSendToPhoneTime(date, messageId);
                session.commit();
            } catch (Exception e) {
                log.severe(e, "Ошибка добавления в базу времени отправления сообщения на телефон!");
            }

        }
    }
//==================================================================================
    /**
     * Добавляет в БД время отправки сообщения.
     * @param date время,добавляемое в базу данных
     * @param messageId id сообщения для которого добавляется время
     */
    public void setSendTime(Date date, Integer messageId) {

        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                mapp.addSendTime(date, messageId);
                session.commit();
            } catch (Exception e) {
                log.severe(e, "Ошибка добавления в базу времени отправления сообщения!");
            }
        }
    }

//=======================================================================================
    
    /**
     * Меняет в Бд статус отправки конкретного сообщения по его id.
     * @param messageId id сообщения для которого меняется статус
     * @param statusString новый статус
     */
    public void setSendStatus(Integer messageId, String statusString) {

        try (SqlSession session = DatabaseConnection.getDatabase().openSession()) {
            try {
                MessageMapper mapp = session.getMapper(MessageMapper.class);
                mapp.changeMessageStatus(messageId, statusString);
                session.commit();
            } catch (Exception e) {
                log.severe(e, "Ошибка изменения статуса отправки сообщения!");
            }

        }
    }

}
