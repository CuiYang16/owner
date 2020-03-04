package priv.cy.owner.service.user;

import priv.cy.owner.entity.sysUserInfo.SysUserInfo;

public interface SysUserInfoService {

    SysUserInfo findUserNameByToken(String UserName);
}
