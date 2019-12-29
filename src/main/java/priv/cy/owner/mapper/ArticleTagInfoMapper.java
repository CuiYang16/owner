package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticleTagInfo;

@Mapper
public interface ArticleTagInfoMapper {
    int insert(ArticleTagInfo record);

    int insertSelective(ArticleTagInfo record);

    int batchInsert(@Param("list") List<ArticleTagInfo> list);
}