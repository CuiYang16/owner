package priv.cy.owner.mapper.sysUserInfoMapper;

import org.apache.ibatis.annotations.Mapper;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;

@Mapper
public interface SysUserInfoPrivMapper {
    // 非自动生成
    SysUserInfo findUserNameByToken(String userName);
}
