package su.vistar.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SenderMapper {
    
    @Select("SELECT sd_id FROM sender WHERE sd_name =  #{sd_name};")
    public int getSenderIdByName(@Param("sd_name") String sd_name);
}
