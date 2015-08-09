package su.vistar.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ApplicationMapper {
    
    @Select("SELECT app_id FROM application WHERE app_name = #{app_name} AND app_key = #{app_key};")
    public int getApplicationIdByLoginAndKey(@Param("app_name") String app_name,@Param("app_key") String app_key);
}
