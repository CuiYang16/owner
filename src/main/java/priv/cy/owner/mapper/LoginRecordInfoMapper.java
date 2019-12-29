package priv.cy.owner.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.cy.owner.entity.LoginRecordInfo;

@Mapper
public interface LoginRecordInfoMapper {
    int insert(LoginRecordInfo record);

    int insertSelective(LoginRecordInfo record);

    int batchInsert(@Param("list") List<LoginRecordInfo> list);
}