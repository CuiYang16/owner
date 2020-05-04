package priv.cy.owner.service.user;

import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;

public interface SysUserInfoService {

    ResultInfo findUserNameByToken(ReqLoginUserInfo reqLoginUserInfo);
}
