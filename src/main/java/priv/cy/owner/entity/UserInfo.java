package priv.cy.owner.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserInfo implements Serializable {
    /**
    * 用户详情表
    */
    private String userId;

    /**
    * 用户名
    */
    private String userName;

    /**
    * 密码
    */
    private String userPwd;

    /**
    * 性别
    */
    private Boolean userSex;

    /**
    * 手机号码
    */
    private String userPhone;

    /**
    * 邮箱
    */
    private String userEmail;

    /**
    * 生日
    */
    private Date userBirthday;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 是否删除
    */
    private Boolean isDeleted;

    /**
    * 是否锁定
    */
    private Boolean isLocked;

    /**
    * 用户头像url
    */
    private String userAvatar;

    /**
    * 盐
    */
    private String pwdSalt;

    private static final long serialVersionUID = 1L;
}