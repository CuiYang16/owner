package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticleTypeInfo;

@Mapper
public interface ArticleTypeInfoMapper {
    int insert(ArticleTypeInfo record);

    int insertSelective(ArticleTypeInfo record);

    int batchInsert(@Param("list") List<ArticleTypeInfo> list);
}