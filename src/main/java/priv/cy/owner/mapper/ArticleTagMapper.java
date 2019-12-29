package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticleTag;

@Mapper
public interface ArticleTagMapper {
    int insert(ArticleTag record);

    int insertSelective(ArticleTag record);

    int batchInsert(@Param("list") List<ArticleTag> list);
}