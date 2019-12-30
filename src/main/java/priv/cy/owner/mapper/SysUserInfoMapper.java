package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.SysUserInfo;

@Mapper
public interface SysUserInfoMapper {
    int deleteByPrimaryKey(String userId);

    int insert(SysUserInfo record);

    int insertSelective(SysUserInfo record);

    SysUserInfo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(SysUserInfo record);

    int updateByPrimaryKey(SysUserInfo record);

    int updateBatch(List<SysUserInfo> list);

    int batchInsert(@Param("list") List<SysUserInfo> list);
}