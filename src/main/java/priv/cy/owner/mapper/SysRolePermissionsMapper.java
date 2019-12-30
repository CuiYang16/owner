package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.SysRolePermissions;

@Mapper
public interface SysRolePermissionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRolePermissions record);

    int insertSelective(SysRolePermissions record);

    SysRolePermissions selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRolePermissions record);

    int updateByPrimaryKey(SysRolePermissions record);

    int updateBatch(List<SysRolePermissions> list);

    int batchInsert(@Param("list") List<SysRolePermissions> list);
}