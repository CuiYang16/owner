package priv.cy.owner.util.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 20:43
 */
public class JwtToken implements AuthenticationToken {
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
