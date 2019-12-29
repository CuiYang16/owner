package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class OperatingRecordInfo implements Serializable {
    /**
    * 操作历史表
    */
    private String operatingRecordId;

    /**
    * 操作名称
    */
    private String operationName;

    /**
    * 操作url
    */
    private String operationUrl;

    /**
    * 操作时间
    */
    private Date operationTime;

    /**
    * 登录记录FK
    */
    private String loginRecordId;

    private static final long serialVersionUID = 1L;
}