package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysRoleInfo implements Serializable {
    private String roleId;

    /**
    * 角色名
    */
    private String roleName;

    /**
    * 父角色id
    */
    private String parentId;

    /**
    * 简介
    */
    private String description;

    /**
    * 是否锁定
    */
    private Boolean isLocked;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}