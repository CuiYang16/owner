package priv.cy.owner.service.role.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.mapper.role.SysRoleInfoPrivMapper;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.service.role.SysUserRoleService;
import priv.cy.owner.service.userrole.UserRoleService;
import priv.cy.owner.util.jwt.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/3/4 14:41
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysRoleInfoPrivMapper sysRoleInfoPrivMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public ResultInfo getRolesByRoleIds(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userName = JwtUtil.getUsername(token);
        List<SysRoleInfo> rolesByRoleIds =
                sysRoleInfoPrivMapper.selectRoleByRoleId(userRoleService.getRoleByUserName(userName));

        List<String> roles = new ArrayList<String>();

        rolesByRoleIds.forEach(r -> {
            roles.add(r.getRoleName());
        });

        return ResultInfo.ok().data("roles", roles).data("userName", userName);

    }


    @Override
    public List<SysRoleInfo> getRolesByUserName(String userName) {

        List<SysRoleInfo> roles =
                sysRoleInfoPrivMapper.selectRoleByRoleId(userRoleService.getRoleByUserName(userName));
        return roles;
    }
}
