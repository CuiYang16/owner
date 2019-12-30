package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.SysPermissionsInfo;

@Mapper
public interface SysPermissionsInfoMapper {
    int deleteByPrimaryKey(String permissionsId);

    int insert(SysPermissionsInfo record);

    int insertSelective(SysPermissionsInfo record);

    SysPermissionsInfo selectByPrimaryKey(String permissionsId);

    int updateByPrimaryKeySelective(SysPermissionsInfo record);

    int updateByPrimaryKey(SysPermissionsInfo record);

    int updateBatch(List<SysPermissionsInfo> list);

    int batchInsert(@Param("list") List<SysPermissionsInfo> list);
}