package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class LoginRecordInfo implements Serializable {
    /**
    * 登录记录表
    */
    private String loginRecordId;

    /**
    * 登录人FK
    */
    private String loginUser;

    /**
    * 登录时间
    */
    private Date loginTime;

    /**
    * 登录ip
    */
    private String loginIp;

    private static final long serialVersionUID = 1L;
}