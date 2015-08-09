package su.vistar.db.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import su.vistar.db.entity.CountEntity;
import su.vistar.db.entity.MessageEntity;

public interface MessageMapper {

    @Select("SELECT * FROM message WHERE msg_status = 'NOT_SEND' AND msg_sender_id = #{msg_sender_id};")
    public List<MessageEntity> selectMessages(@Param("msg_sender_id") int msg_sender_id);

    @Insert("INSERT INTO message ( msg_text, msg_phone_numbers, msg_sender_id,msg_sender_name,msg_status, "
            + "msg_comment, msg_creation_date,msg_send_to_phone_date,msg_send_date,msg_app_id)"
            + " VALUES ( #{msg_text}, #{msg_phone_numbers}, #{msg_sender_id}, #{msg_sender_name}, "
            + "#{msg_status}, #{msg_comment}, #{msg_creation_date},#{msg_send_to_phone_date},#{msg_send_date},#{msg_app_id}) ;")
    public void addMessageSettings(@Param("msg_text") String msg_text,
            @Param("msg_phone_numbers") String msg_phone_numbers, @Param("msg_sender_id") int msg_sender_id,
            @Param("msg_sender_name") String msg_sender_name,@Param("msg_status") String msg_status, @Param("msg_comment") String msg_comment,
            @Param("msg_creation_date") Date msg_creation_date,@Param("msg_send_to_phone_date") Date msg_send_to_phone_date,
            @Param("msg_send_date") Date msg_send_date,@Param("msg_app_id") int msg_app_id
            );
    
    @Insert("UPDATE message SET msg_send_to_phone_date = #{msg_send_to_phone_date} WHERE msg_id = #{msg_id};")
    public void addSendToPhoneTime(@Param("msg_send_to_phone_date") Date msg_send_to_phone_date,@Param("msg_id") int msg_id);
    
    @Insert("UPDATE message SET msg_send_date = #{msg_send_date} WHERE msg_id = #{msg_id};")
    public void addSendTime(@Param("msg_send_date") Date msg_send_date,@Param("msg_id") int msg_id);
    
    @Update("UPDATE message SET msg_status = #{msg_status} WHERE msg_id = #{msg_id} ;")
    public void changeMessageStatus(@Param("msg_id") int msg_id,@Param("msg_status") String msg_status);

    @Select("SELECT msg_status,COUNT(*)cnt FROM message WHERE  msg_creation_date >= ( NOW() - INTERVAL 1 HOUR )AND msg_app_id = #{msg_app_id} GROUP BY msg_status;")
    public List<CountEntity> getStatusesCountByAppId(@Param("msg_app_id") int msg_app_id);
    
    @Select("SELECT COUNT(*) FROM message WHERE (msg_creation_date >= ( NOW() - INTERVAL #{minutes} MINUTE ) AND msg_text = #{msg_text} AND msg_app_id = #{msg_app_id}) "
            + "AND ((msg_status = 'SEND_TO_PHONE') OR (msg_status = 'NOT_SEND')) ;")
    public int getSameMessageCount(@Param("msg_text") String msg_text,@Param("msg_app_id") int msg_app_id,@Param("minutes") int minutes);
}
