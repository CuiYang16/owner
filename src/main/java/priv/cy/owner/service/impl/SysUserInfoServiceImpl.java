package priv.cy.owner.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.sysUserInfoMapper.SysUserInfoMapper;
import priv.cy.owner.mapper.sysUserInfoMapper.SysUserInfoPrivMapper;
import priv.cy.owner.service.SysUserInfoService;

import javax.annotation.Resource;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/4 13:57
 */
@Service
public class SysUserInfoServiceImpl implements SysUserInfoService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserInfoServiceImpl.class);

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Resource
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;

    @Override
    public SysUserInfo findUserNameByToken(String userName) {
        SysUserInfo loginUserInfo = sysUserInfoMapperPriv.findUserNameByToken(userName);

        System.out.println(loginUserInfo.getUserName());
        return loginUserInfo;
    }
}
