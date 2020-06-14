package priv.cy.owner.web.controller.sysuser;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.cy.owner.common.jwt.CommonConstant;
import priv.cy.owner.common.jwt.JwtProperties;
import priv.cy.owner.common.jwt.JwtUtil;
import priv.cy.owner.common.util.md5.MD5Util;
import priv.cy.owner.common.util.random.RandImageUtil;
import priv.cy.owner.common.util.redis.RedisUtil;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.model.ResultCodeEnum;
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

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

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
     * key请求验证码的时间
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/randomImage/{key}", method = RequestMethod.GET)
    public ResultInfo randomImage(@PathVariable String key) {
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            return ResultInfo.ok().data("image", base64);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.setResult(ResultCodeEnum.CAPTCHA_GET_ERROR);
        }
    }

    /**
     * 用户角色信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResultInfo sysUserInfo(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, request.getHeader("Authorization"));
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        return sysUserRoleService.getRolesByRoleIds(request);
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResultInfo loginOut(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (StrUtil.isEmpty(token)) {
            return ResultInfo.setResult(ResultCodeEnum.LOGOUT_FAIL);
        }
        String username = JwtUtil.getUsername(token);
        if (username != null) {
            logger.info(" 用户名:  " + username + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE);
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return ResultInfo.setResult(ResultCodeEnum.USER_LOGOUT_SUCCESS);
        } else {
            return ResultInfo.setResult(ResultCodeEnum.USER_TOKEN_INCORRECT);
        }
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

    @RequiresRoles(value = "admin")
    @RequestMapping(value = "/deluser", method = RequestMethod.DELETE)
    public ResultInfo deleteSysUser(String userId) {
        ResultInfo resultInfo = sysUserInfoService.deleteSysUser(userId);
        return resultInfo;
    }
}

