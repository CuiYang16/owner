package priv.cy.owner.web.controller.sysuser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import priv.cy.owner.mapper.sysUserInfoMapper.SysUserInfoMapperPriv;
import priv.cy.owner.model.ResponseModel;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/5 13:18
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserController {

  @Autowired private SysUserInfoMapperPriv sysUserInfoMapperPriv;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseModel sysUserLogin(@RequestBody ReqLoginUserInfo reqLoginUserInfo) {

    Subject subject = SecurityUtils.getSubject();

    UsernamePasswordToken usernamePasswordToken =
        new UsernamePasswordToken(reqLoginUserInfo.getUserName(), reqLoginUserInfo.getPassWord());

    try {
      subject.login(usernamePasswordToken);
      return ResponseModel.builder().code(1).msg("").build();
    } catch (UnknownAccountException u) {
      return ResponseModel.builder().code(0).msg(u.getMessage()).build();
    } catch (LockedAccountException l) {
      return ResponseModel.builder().code(0).msg(l.getMessage()).build();
    }
  }
}
