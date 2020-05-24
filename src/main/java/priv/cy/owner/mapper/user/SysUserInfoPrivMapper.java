package priv.cy.owner.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;

import java.util.List;

@Mapper
@Component
public interface SysUserInfoPrivMapper {
    // 非自动生成
    SysUserInfo findUserNameByToken(String userName);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<SysUserInfo> selectAllUser();

    /**
     * 创建新用户
     *
     * @param sysUserInfo
     */
    String insertSysUser(SysUserInfo sysUserInfo);
}
