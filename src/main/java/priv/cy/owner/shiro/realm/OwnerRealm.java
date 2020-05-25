package priv.cy.owner.shiro.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import priv.cy.owner.common.jwt.CommonConstant;
import priv.cy.owner.common.jwt.JwtToken;
import priv.cy.owner.common.jwt.JwtUtil;
import priv.cy.owner.common.util.MyConvertUtils;
import priv.cy.owner.common.util.SpringContextUtils;
import priv.cy.owner.common.util.redis.RedisUtil;
import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.service.role.SysUserRoleService;

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
        String token = (String) authenticationToken.getCredentials();
        if (token == null) {
            logger.info("————————身份认证失败——————————IP地址:  " + MyConvertUtils.getIpAddrByRequest(SpringContextUtils.getHttpServletRequest()));
            throw new AuthenticationException("token为空!");

        }

        SysUserInfo loginUser = this.checkUserTokenIsEffect(token);


        return new SimpleAuthenticationInfo(loginUser, token, getName());
        //}
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        logger.info("===============Shiro权限认证开始============ [ roles、permissions]==========");
        String username = null;
        if (principalCollection != null) {
            SysUserInfo sysUser = (SysUserInfo) principalCollection.getPrimaryPrincipal();
            username = sysUser.getUserName();
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 此处如果多个角色都拥有某项权限，bu会数据重复，内部用的是Set
        List<SysRoleInfo> roles = sysUserRoleService.getRolesByUserName(username);

        roles.forEach(r -> {
            logger.debug(r.getRoleName());
            authorizationInfo.addRole(r.getRoleName());
        });
        authorizationInfo.addStringPermission("管理员");

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

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public SysUserInfo checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        // 查询用户信息
        logger.debug("———校验token是否有效————checkUserTokenIsEffect——————— " + token);
        SysUserInfo loginUser = sysUserInfoMapperPriv.findUserNameByToken(username);
        if (loginUser == null) {
            throw new AuthenticationException("用户不存在!");
        }
        // 用户不存在
        if (ObjectUtil.isNull(loginUser)
                || StrUtil.hasBlank(loginUser.getUserPwd())
                || StrUtil.hasBlank(loginUser.getPwdSalt())) {
            throw new UnknownAccountException("用户名或密码错误，请确认后重试！");
        }

        // 账户锁定
        if (loginUser.getIsLocked() == 1) {
            throw new LockedAccountException("账户被锁定，请联系管理员！");
        }

        // 账户删除
        if (loginUser.getIsDeleted() == 1) {
            throw new UnknownAccountException("账户状态异常，请联系管理员！");
        }

        return loginUser;
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     * 用户过期时间 = Jwt有效时间 * 2。
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (StrUtil.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置超时时间
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
                logger.info("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— " + token);
            }
            //update-begin--Author:scott  Date:20191005  for：解决每次请求，都重写redis中 token缓存问题
//			else {
//				// 设置超时时间
//				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
//				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
//			}
            //update-end--Author:scott  Date:20191005   for：解决每次请求，都重写redis中 token缓存问题
            return true;
        }
        return false;
    }

}
