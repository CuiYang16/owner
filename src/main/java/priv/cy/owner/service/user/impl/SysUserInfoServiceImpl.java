package priv.cy.owner.service.user.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.cy.owner.common.jwt.JwtProperties;
import priv.cy.owner.common.jwt.JwtToken;
import priv.cy.owner.common.jwt.JwtUtil;
import priv.cy.owner.common.util.md5.MD5Util;
import priv.cy.owner.common.util.redis.RedisUtil;
import priv.cy.owner.common.util.uuid.UUIDutil;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.model.ResultCodeEnum;
import priv.cy.owner.model.ResultInfo;
import priv.cy.owner.model.sysuser.ReqLoginUserInfo;
import priv.cy.owner.service.user.SysUserInfoService;

import java.util.ArrayList;
import java.util.List;

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
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public ResultInfo findUserNameByToken(ReqLoginUserInfo reqLoginUserInfo) {

        String userPwd = String.valueOf(new SimpleHash(HASH_ALGORITH_NAME,
                reqLoginUserInfo.getPassWord(), SALT, HASHI_TERATIONS));
        String token = JwtUtil.sign(reqLoginUserInfo.getUserName(), userPwd, reqLoginUserInfo.getRememberMe());

        SysUserInfo loginUser = sysUserInfoMapperPriv.findUserNameByToken(reqLoginUserInfo.getUserName());

        JwtToken jwtToken = new JwtToken(token);
        logger.debug(loginUser.getUserName());
        String captcha = reqLoginUserInfo.getCaptcha();

        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + reqLoginUserInfo.getCheckKey(), "utf-8");
        String checkCode = (String) redisUtil.get(realKey);

        if (!ObjectUtil.isNull(loginUser)) {
            if (StrUtil.hasBlank(loginUser.getUserName())) {
                return ResultInfo.setResult(ResultCodeEnum.LOGINUSER_INFO_ERROR);
            }
            if (StrUtil.hasBlank(loginUser.getUserPwd())) {
                return ResultInfo.setResult(ResultCodeEnum.LOGINUSER_INFO_ERROR);
            }
            if (StrUtil.hasBlank(loginUser.getUserName())) {
                return ResultInfo.setResult(ResultCodeEnum.LOGINUSER_INFO_ERROR);
            }
            if (StrUtil.isEmpty(captcha)) {
                return ResultInfo.setResult(ResultCodeEnum.RANDOM_IMAGE_ERROR);
            }
            if (StrUtil.isEmpty(lowerCaseCaptcha) || !lowerCaseCaptcha.equalsIgnoreCase(checkCode)) {
                return ResultInfo.setResult(ResultCodeEnum.RANDOM_IMAGE_ERROR);
            }

            //redisUtil.set(loginUser.getUserName(), token, REFRESH_JWT_TOKEN_EXPIRE_TIME);
            return ResultInfo.ok().data("token", token).message("登录成功");
        } else {
            return ResultInfo.setResult(ResultCodeEnum.USER_NEED_AUTHORITIES);
        }

    }

    @Override
    public PageInfo<SysUserInfo> getAllUsers(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<SysUserInfo> sysUserInfos = sysUserInfoMapperPriv.selectAllUser();
        PageInfo<SysUserInfo> pageInfo = new PageInfo<SysUserInfo>(sysUserInfos);
        pageInfo.setPageNum(currentPage);
        pageInfo.setPageSize(pageSize);

        if (sysUserInfos.size() <= 0) {
            pageInfo.setList(new ArrayList<>());
        }
        return pageInfo;
    }

    @Transactional
    @Override
    public ResultInfo createSysUser(SysUserInfo sysUserInfo) {
        sysUserInfo.setUserId(UUIDutil.getUUID());
        sysUserInfo.setUserPwd(String.valueOf(new SimpleHash("MD5", sysUserInfo.getUserPwd(), ByteSource.Util.bytes("cy_salt"),
                1024)));
        sysUserInfo.setPwdSalt("cy_salt");
        logger.debug(sysUserInfo.toString());
        sysUserInfoMapperPriv.insertSysUser(sysUserInfo);

        return ResultInfo.ok();
    }
}
