package priv.cy.owner.service.role;

import priv.cy.owner.model.ResultInfo;

import javax.servlet.http.HttpServletRequest;

public interface SysUserRoleService {
    ResultInfo getRolesByRoleIds(HttpServletRequest request);
}
