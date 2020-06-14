package priv.cy.owner.service.user;

import com.github.pagehelper.PageInfo;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;

public interface SysUserInfoService {

    ResultInfo findUserNameByToken(ReqLoginUserInfo reqLoginUserInfo);

    PageInfo<SysUserInfo> getAllUsers(Integer currentPage, Integer pageSize);

    ResultInfo createSysUser(SysUserInfo sysUserInfo);

    ResultInfo deleteSysUser(String userId);
}
