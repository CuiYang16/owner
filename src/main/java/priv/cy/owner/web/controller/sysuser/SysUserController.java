package priv.cy.owner.web.controller.sysuser;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import priv.cy.owner.common.jwt.JwtProperties;
import priv.cy.owner.common.util.redis.RedisUtil;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;
import priv.cy.owner.service.role.SysUserRoleService;
import priv.cy.owner.service.user.SysUserInfoService;
import priv.cy.owner.service.userrole.UserRoleService;

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

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);
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

    @RequiresRoles(value = "admin")
    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public ResultInfo createSysUser(@RequestBody String sysUserJson) {
        logger.debug(sysUserJson);
        SysUserInfo sysUserInfo = JSON.parseObject(sysUserJson, SysUserInfo.class);
        ResultInfo resultInfo = sysUserInfoService.createSysUser(sysUserInfo);
        return resultInfo;
    }

}

