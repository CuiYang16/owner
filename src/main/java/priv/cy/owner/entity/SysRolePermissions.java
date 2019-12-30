package priv.cy.owner.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysRolePermissions implements Serializable {
    private Integer id;

    /**
    * 角色id
    */
    private Integer roleId;

    /**
    * 权限id
    */
    private Integer permissionsId;

    private static final long serialVersionUID = 1L;
}