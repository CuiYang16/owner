package priv.cy.owner.web.rest.sysuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.cy.owner.service.SysUserInfoService;

/**
 * @author ：cuiyang
 * @description：用户接口
 * @date ：Created in 2020/1/5 13:03
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserRest {

  @Autowired private SysUserInfoService sysUserInfoService;
}
