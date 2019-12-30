package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.SysRoleInfo;

@Mapper
public interface SysRoleInfoMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(SysRoleInfo record);

    int insertSelective(SysRoleInfo record);

    SysRoleInfo selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(SysRoleInfo record);

    int updateByPrimaryKey(SysRoleInfo record);

    int updateBatch(List<SysRoleInfo> list);

    int batchInsert(@Param("list") List<SysRoleInfo> list);
}