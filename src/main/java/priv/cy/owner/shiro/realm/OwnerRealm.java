package priv.cy.owner.shiro.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.service.SysUserInfoService;
import priv.cy.owner.util.jwt.JwtToken;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 21:00
 */
@Component
public class OwnerRealm extends AuthorizingRealm {

  @Autowired private SysUserInfoService sysUserInfoService;

  /** 必须重写此方法，不然Shiro会报错 */
  @Override
  public boolean supports(AuthenticationToken token) {

    return token instanceof JwtToken;
  }

  /** 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的 */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {

    String userName = (String) authenticationToken.getPrincipal();

    if (StrUtil.hasBlank(userName)) {
      throw new UnsupportedTokenException("未知token");
    }

    SysUserInfo sysUserInfo = sysUserInfoService.findUserNameByToken(userName);

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

    SimpleAuthenticationInfo authenticationInfo =
        new SimpleAuthenticationInfo(
            sysUserInfo,
            sysUserInfo.getUserPwd(),
            ByteSource.Util.bytes(sysUserInfo.getPwdSalt()),
            getName());

    return authenticationInfo;
  }

  /** 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。 */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

    SysUserInfo sysUserInfo = (SysUserInfo) principalCollection.getPrimaryPrincipal();
    try {

      // 此处如果多个角色都拥有某项权限，bu会数据重复，内部用的是Set

    } catch (Exception e) {
      e.printStackTrace();
    }
    // return authorizationInfo;
    return null;
  }
}
