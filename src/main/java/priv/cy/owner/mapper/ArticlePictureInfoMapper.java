package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.ArticlePictureInfo;

@Mapper
public interface ArticlePictureInfoMapper {
    int insert(ArticlePictureInfo record);

    int insertSelective(ArticlePictureInfo record);

    int batchInsert(@Param("list") List<ArticlePictureInfo> list);
}