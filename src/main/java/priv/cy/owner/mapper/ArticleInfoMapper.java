package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticleInfo;

@Mapper
public interface ArticleInfoMapper {
    int insert(ArticleInfo record);

    int insertSelective(ArticleInfo record);

    int batchInsert(@Param("list") List<ArticleInfo> list);
}