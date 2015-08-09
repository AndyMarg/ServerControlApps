package su.vistar.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import su.vistar.db.entity.QuoteEntity;

public interface QuoteMapper {

    @Select("SELECT * FROM quote WHERE qt_app_id = #{qt_app_id}; ")
    public QuoteEntity getQuotesByAppId(@Param("qt_app_id") int qt_app_id);
}
