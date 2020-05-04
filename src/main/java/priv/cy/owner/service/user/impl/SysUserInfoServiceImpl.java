package priv.cy.owner.service.user.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoMapper;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;
import priv.cy.owner.service.user.SysUserInfoService;
import priv.cy.owner.util.jwt.JwtProperties;
import priv.cy.owner.util.jwt.JwtToken;
import priv.cy.owner.util.jwt.JwtUtil;
import priv.cy.owner.util.redis.RedisUtil;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/4 13:57
 */
@Service
public class SysUserInfoServiceImpl implements SysUserInfoService {

    private static final String HASH_ALGORITH_NAME = "MD5";
    private static final int HASHI_TERATIONS = 1024;
    private static final ByteSource SALT = ByteSource.Util.bytes("cy_salt");
    private static final long REFRESH_JWT_TOKEN_EXPIRE_TIME = 30L;
    private static final Logger logger = LoggerFactory.getLogger(SysUserInfoServiceImpl.class);

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public ResultInfo findUserNameByToken(ReqLoginUserInfo reqLoginUserInfo) {

        String userPwd = String.valueOf(new SimpleHash(HASH_ALGORITH_NAME,
                reqLoginUserInfo.getPassWord(), SALT, HASHI_TERATIONS));
        String token = JwtUtil.sign(reqLoginUserInfo.getUserName(), userPwd);

        SysUserInfo loginUser = sysUserInfoMapperPriv.findUserNameByToken(reqLoginUserInfo.getUserName());

        JwtToken jwtToken = new JwtToken(token);

        if (!ObjectUtil.isNull(loginUser)
                && !StrUtil.hasBlank(loginUser.getUserPwd())
                && !StrUtil.hasBlank(loginUser.getPwdSalt())) {

            redisUtil.set(loginUser.getUserName(), token, REFRESH_JWT_TOKEN_EXPIRE_TIME);
            return ResultInfo.ok().data("token", token).message("登录成功");
        } else {
            throw new RuntimeException("请刷新重试.");
        }
    }
}
