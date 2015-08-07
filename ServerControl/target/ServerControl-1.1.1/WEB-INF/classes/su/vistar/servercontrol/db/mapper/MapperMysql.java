package su.vistar.servercontrol.db.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import su.vistar.servercontrol.entity.ComponentEntity;

public interface MapperMysql {

    @Select("SELECT * FROM SC_settings WHERE component = #{component_name}")
    public List<ComponentEntity> selectComponentSettings(String component_name);

    @Insert("INSERT INTO SC_settings (component, name, value) VALUES ( #{component}, #{name}, #{value} ) ;")
    void addComponentSettings(@Param("component") String component,@Param("name") String name,@Param("value") String value);
}
