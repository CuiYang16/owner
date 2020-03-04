package priv.cy.owner.service.userrole.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.mapper.userrole.SysUserRolePrivMapper;
import priv.cy.owner.service.userrole.UserRoleService;

import java.util.List;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/3/4 14:56
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysUserRolePrivMapper sysUserRolePrivMapper;

    @Autowired
    private SysUserInfoPrivMapper sysUserInfoPrivMapper;

    @Override
    public List<String> getRoleByUserName(String userName) {
        try {
            String userId = sysUserInfoPrivMapper.findUserNameByToken(userName).getUserId();
            List<String> roleIds = sysUserRolePrivMapper.selectRoleIdByUserId(userId);
            if (!roleIds.isEmpty()) {
                return roleIds;
            }
            throw new AuthenticationException("未分配用户角色！");
        } catch (Exception e) {

            return null;
        }

    }
}
