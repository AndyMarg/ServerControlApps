package su.vistar.db.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import su.vistar.db.entity.SenderPhoneEntity;

public interface SenderPhoneMapper {
    
    @Select("SELECT sdp_imei FROM sender_phone WHERE sdp_sender_id = #{sdp_sender_id};")
    public List<Integer> getImeiById(@Param("sdp_sender_id") int sdp_sender_id);
    
    @Select("SELECT sdp_sender_id FROM sender_phone WHERE sdp_imei = #{sdp_imei} AND sdp_password = #{sdp_password};")
    public int getIdByImei(@Param("sdp_imei") String sdp_imei,@Param("sdp_password") String sdp_password);
    
    @Select("SELECT * FROM sender_phone WHERE sdp_imei = #{sdp_imei} AND sdp_password = #{sdp_password}; ")
    public SenderPhoneEntity getInfoByPasswordAndImei(@Param("sdp_imei") String sdp_imei,@Param("sdp_password") String sdp_password);
}
