package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    int batchInsert(@Param("list") List<UserInfo> list);
}