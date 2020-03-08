package priv.cy.owner.web.controller.sysuser;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import priv.cy.owner.entity.SysRoleInfo;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.model.ResponseModel;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;
import priv.cy.owner.model.sysuser.ResUserInfo;
import priv.cy.owner.service.role.SysUserRoleService;
import priv.cy.owner.service.userrole.UserRoleService;
import priv.cy.owner.util.jwt.JwtProperties;
import priv.cy.owner.util.jwt.JwtToken;
import priv.cy.owner.util.jwt.JwtUtil;
import priv.cy.owner.util.redis.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/5 13:18
 */
@RestController
@RequestMapping("/sysuser")
public class SysUserController {

    private static final String HASH_ALGORITH_NAME = "MD5";
    private static final int HASHI_TERATIONS = 1024;
    private static final ByteSource SALT = ByteSource.Util.bytes("cy_salt");
    private static final long REFRESH_JWT_TOKEN_EXPIRE_TIME = 30L;

    @Autowired
    private SysUserInfoPrivMapper sysUserInfoPrivMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseModel sysUserLogin(@RequestBody ReqLoginUserInfo reqLoginUserInfo, HttpServletResponse response) {

        String userPwd = String.valueOf(new SimpleHash(HASH_ALGORITH_NAME,
                reqLoginUserInfo.getPassWord(), SALT, HASHI_TERATIONS));
        String token = JwtUtil.sign(reqLoginUserInfo.getUserName(), userPwd);

        SysUserInfo loginUser = sysUserInfoPrivMapper.findUserNameByToken(reqLoginUserInfo.getUserName());

        JwtToken jwtToken = new JwtToken(token);
        //UsernamePasswordToken usernamePasswordToken =
        //        new UsernamePasswordToken(reqLoginUserInfo.getUserName(), userPwd);
        //usernamePasswordToken.setRememberMe(true);
        //Subject subject = SecurityUtils.getSubject();
        try {

            //subject.login(usernamePasswordToken);
            if (!ObjectUtil.isNull(loginUser)
                    && !StrUtil.hasBlank(loginUser.getUserPwd())
                    && !StrUtil.hasBlank(loginUser.getPwdSalt())) {
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                response.setHeader("Authorization", token);

                String currentTimeMillis = String.valueOf(System.currentTimeMillis());

                //redisUtil.set(reqLoginUserInfo.getUserName(), currentTimeMillis, jwtProperties.getTokenExpireTime() * 60);
                redisUtil.set(loginUser.getUserName(), token, REFRESH_JWT_TOKEN_EXPIRE_TIME);
                return ResponseModel.builder().code(20000).msg("").build();
            } else {
                throw new RuntimeException("请刷新重试.");
            }

        } catch (UnknownAccountException u) {
            return ResponseModel.builder().code(0).msg(u.getMessage()).build();
        } catch (LockedAccountException l) {
            return ResponseModel.builder().code(0).msg(l.getMessage()).build();
        } catch (Exception e) {
            return ResponseModel.builder().code(0).msg(e.getMessage()).build();
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseModel sysUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userName = JwtUtil.getUsername(token);
        List<SysRoleInfo> rolesByRoleIds = sysUserRoleService.getRolesByRoleIds(userRoleService.getRoleByUserName(userName));

        List<String> roles = new ArrayList<String>();

        rolesByRoleIds.forEach(r -> {
            roles.add(r.getRoleName());
        });

        return ResponseModel.builder().code(20000).msg("").list(roles)
                .resObj(ResUserInfo.builder().userName(userName).userAvatar("E:\\IdeaWorkSpace\\MyWorkSpace\\owner\\src\\main" +
                        "\\resources\\static\\images\\avatar\\head_default.jpg").introduction("vbbbb").build()).build();
    }
}

