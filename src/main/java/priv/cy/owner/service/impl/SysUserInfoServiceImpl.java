package priv.cy.owner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.sysUserInfoMapper.SysUserInfoMapper;
import priv.cy.owner.mapper.sysUserInfoMapper.SysUserInfoMapperPriv;
import priv.cy.owner.service.SysUserInfoService;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/1/4 13:57
 */
@Service
public class SysUserInfoServiceImpl implements SysUserInfoService {

  @Autowired private SysUserInfoMapper sysUserInfoMapper;

  @Autowired private SysUserInfoMapperPriv sysUserInfoMapperPriv;

  @Override
  public SysUserInfo findUserNameByToken(String userName) {
    SysUserInfo loginUserInfo = sysUserInfoMapperPriv.findUserNameByToken(userName);

    return null;
  }
}
