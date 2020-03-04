package priv.cy.owner.service.role.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.mapper.role.SysRoleInfoPrivMapper;
import priv.cy.owner.service.role.SysUserRoleService;

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

    @Override
    public List<SysRoleInfo> getRolesByRoleIds(List<String> roleIds) {
        List<SysRoleInfo> sysRoleInfos = sysRoleInfoPrivMapper.selectRoleByRoleId(roleIds);
        if (!sysRoleInfos.isEmpty()) {
            return sysRoleInfos;
        }
        return null;
    }
}
