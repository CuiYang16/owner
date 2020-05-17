package priv.cy.owner.entity.sysUserInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserInfo implements Serializable {
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
    private Integer userSex;

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
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd ")
    private Date userBirthday;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 是否锁定
     */
    private Integer isLocked;

    /**
     * 用户头像url
     */
    private String userAvatar;

    /**
     * 盐
     */
    private String pwdSalt;

    @JsonFormat(timezone = "GMT+8", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
