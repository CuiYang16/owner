package priv.cy.owner.sysuser;

import org.springframework.beans.factory.annotation.Autowired;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2020/3/1 17:51
 */
public class LoginUser {

    @Autowired
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;

    public SysUserInfo aaa() {

        SysUserInfo user = sysUserInfoMapperPriv.findUserNameByToken("admin");
        return user;
    }

    public static void main(String[] args) {
        LoginUser loginUser = new LoginUser();
        System.out.println(loginUser.aaa());
    }
}
