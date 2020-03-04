package priv.cy.owner.service.role;

import priv.cy.owner.entity.SysRoleInfo;

import java.util.List;

public interface SysUserRoleService {
    List<SysRoleInfo> getRolesByRoleIds(List<String> roleIds);
}
