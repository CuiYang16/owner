package priv.cy.owner.service.role;

import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.model.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysUserRoleService {
    ResultInfo getRolesByRoleIds(HttpServletRequest request);

    List<SysRoleInfo> getRolesByUserName(String userName);
}
