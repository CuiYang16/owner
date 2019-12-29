package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticleConetntInfo;

@Mapper
public interface ArticleConetntInfoMapper {
    int insert(ArticleConetntInfo record);

    int insertSelective(ArticleConetntInfo record);

    int batchInsert(@Param("list") List<ArticleConetntInfo> list);
}