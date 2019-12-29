package priv.cy.owner.mapper;

import org.apache.ibatis.annotations.Mapper;
import priv.cy.owner.entity.UserInfo;

@Mapper
public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);
}