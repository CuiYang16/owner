package priv.cy.owner.shiro.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.service.role.SysUserRoleService;
import priv.cy.owner.util.jwt.JwtToken;
import priv.cy.owner.util.redis.RedisUtil;

import java.util.List;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 21:00
 */
@Component
public class OwnerRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(OwnerRealm.class);


    @Autowired
    @Lazy
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        String userName = (String) authenticationToken.getPrincipal();
        if (StrUtil.hasBlank(userName)) {
            throw new UnsupportedTokenException("未知token");
        }

        SysUserInfo sysUserInfo = sysUserInfoMapperPriv.findUserNameByToken(userName);

        // 用户不存在
        if (ObjectUtil.isNull(sysUserInfo)
                || StrUtil.hasBlank(sysUserInfo.getUserPwd())
                || StrUtil.hasBlank(sysUserInfo.getPwdSalt())) {
            throw new UnknownAccountException("用户名或密码错误，请确认后重试！");
        }

        // 账户锁定
        if (sysUserInfo.getIsLocked() == 1) {
            throw new LockedAccountException("账户被锁定，请联系管理员！");
        }

        // 账户删除
        if (sysUserInfo.getIsDeleted() == 1) {
            throw new UnknownAccountException("账户状态异常，请联系管理员！");
        }

        //缓存是否存在
        if (redisUtil.hasKey(userName)) {
            SimpleAuthenticationInfo authenticationInfo =
                    new SimpleAuthenticationInfo(
                            (Object) sysUserInfo.getUserName(),
                            (Object) sysUserInfo.getUserPwd(),
                            ByteSource.Util.bytes(sysUserInfo.getPwdSalt()),
                            getName());
            logger.debug(userName + "redis存在token");
            return authenticationInfo;
        }
        throw new AuthenticationException("Token expired or incorrect.");

    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        logger.info("----------授权------------------->" + principalCollection.getPrimaryPrincipal());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String userName = (String) principalCollection.getPrimaryPrincipal();
        try {

            // 此处如果多个角色都拥有某项权限，bu会数据重复，内部用的是Set
            List<SysRoleInfo> roles = sysUserRoleService.getRolesByUserName(userName);

            roles.forEach(r -> {
                logger.debug(r.getRoleName());
                authorizationInfo.addRole(r.getRoleName());
            });
            authorizationInfo.addStringPermission("管理员");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return authorizationInfo;
        logger.debug("auth");
        return authorizationInfo;
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     *
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     *
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
