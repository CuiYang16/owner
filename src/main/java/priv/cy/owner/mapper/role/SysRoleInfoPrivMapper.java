package priv.cy.owner.mapper.role;

import org.apache.ibatis.annotations.Mapper;
import priv.cy.owner.entity.SysRoleInfo;

import java.util.List;

@Mapper
public interface SysRoleInfoPrivMapper {

    List<SysRoleInfo> selectRoleByRoleId(List<String> roleIds);
}