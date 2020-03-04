package priv.cy.owner.mapper.userrole;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRolePrivMapper {

    List<String> selectRoleIdByUserId(String UserId);
}