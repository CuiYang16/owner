package com.cy.owner.mapper;

import com.cy.owner.domain.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
    int deleteByPrimaryKey(String userId);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}