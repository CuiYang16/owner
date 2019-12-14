package com.cy.owner.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
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

    private Date userBirthday;

    private static final long serialVersionUID = 1L;
}