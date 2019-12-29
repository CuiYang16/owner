package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.CommentInfo;

@Mapper
public interface CommentInfoMapper {
    int insert(CommentInfo record);

    int insertSelective(CommentInfo record);

    int batchInsert(@Param("list") List<CommentInfo> list);
}