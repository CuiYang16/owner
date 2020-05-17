package priv.cy.owner.web.controller.sysuser;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;
import priv.cy.owner.service.role.SysUserRoleService;
import priv.cy.owner.service.user.SysUserInfoService;
import priv.cy.owner.service.userrole.UserRoleService;
import priv.cy.owner.util.jwt.JwtProperties;
import priv.cy.owner.util.redis.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/5 13:18
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserController {


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param reqLoginUserInfo
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultInfo sysUserLogin(@RequestBody ReqLoginUserInfo reqLoginUserInfo, HttpServletResponse response) {
        ResultInfo userNameByToken = sysUserInfoService.findUserNameByToken(reqLoginUserInfo);
        return userNameByToken;
    }

    /**
     * 用户角色信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResultInfo sysUserInfo(HttpServletRequest request) {
        return sysUserRoleService.getRolesByRoleIds(request);
    }

    /**
     * 权限不足
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/unauth", method = RequestMethod.GET)
    public ResultInfo userUnAuth() {
        return ResultInfo.error().code(51005).message("权限不足请重试！");
    }

    @RequiresRoles(value = "admin")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResultInfo allSysUser(Integer currentPage, Integer pageSize) {
        return ResultInfo.ok().data("pageInfo", sysUserInfoService.getAllUsers(currentPage
                , pageSize));
    }
}

