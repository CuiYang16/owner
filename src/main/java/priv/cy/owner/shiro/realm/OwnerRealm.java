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
import org.springframework.stereotype.Component;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.service.user.SysUserInfoService;
import priv.cy.owner.util.jwt.JwtToken;
import priv.cy.owner.util.jwt.JwtUtil;
import priv.cy.owner.util.redis.RedisUtil;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 21:00
 */
@Component
public class OwnerRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(OwnerRealm.class);

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;
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
        String token = (String) authenticationToken.getPrincipal();
        String userName = JwtUtil.getUsername((String) authenticationToken.getPrincipal());
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
        if (sysUserInfo.getIsLocked()) {
            throw new LockedAccountException("账户被锁定，请联系管理员！");
        }

        // 账户删除
        if (sysUserInfo.getIsDeleted()) {
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
            logger.debug("authenticationInfo");
            return authenticationInfo;
        }
        throw new AuthenticationException("Token expired or incorrect.");

    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        logger.info("----------------------------->" + principalCollection.getPrimaryPrincipal());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUserInfo sysUserInfo = (SysUserInfo) principalCollection.getPrimaryPrincipal();
        try {

            // 此处如果多个角色都拥有某项权限，bu会数据重复，内部用的是Set

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return authorizationInfo;
        logger.debug("auth");
        return authorizationInfo;
    }
}
