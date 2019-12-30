package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysPermissionsInfo implements Serializable {
    /**
    * 权限
    */
    private String permissionsId;

    /**
    * 权限名
    */
    private String permissionsName;

    /**
    * 简介
    */
    private String description;

    /**
    * 是否锁定
    */
    private Boolean isLocked;

    /**
    * 是否删除
    */
    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    /**
    * 0：目录   1：菜单   2：按钮'
    */
    private Integer permissionsType;

    /**
    * 路径
    */
    private String url;

    /**
    * 图标
    */
    private String icon;

    private String parentId;

    /**
    * 排序
    */
    private Integer orderNum;

    private static final long serialVersionUID = 1L;
}