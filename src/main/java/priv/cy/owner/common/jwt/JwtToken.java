package priv.cy.owner.common.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ：cuiyang
 * @description：JwtToken
 * @date ：Created in 2019/12/30 20:43
 */
public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
