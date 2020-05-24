package priv.cy.owner.common.jwt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;
import priv.cy.owner.exception.CMSException;
import priv.cy.owner.mapper.user.SysUserInfoPrivMapper;
import priv.cy.owner.model.ResultCodeEnum;
import priv.cy.owner.util.jwt.JwtToken;
import priv.cy.owner.util.jwt.JwtUtil;
import priv.cy.owner.util.redis.RedisUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2019/1/6.
 */
@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {

    //白名单

    private String anonymousStr = "sysuser/login,sysuser/info,sysuser/logout";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysUserInfoPrivMapper sysUserInfoMapperPriv;

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String contextPath = WebUtils.getPathWithinApplication(WebUtils.toHttp(servletRequest));

        if (!StringUtils.isEmpty(anonymousStr)) {
            String[] anonUrls = anonymousStr.split(",");
            //匿名可访问的url
            for (int i = 0; i < anonUrls.length; i++) {
                if (contextPath.contains(anonUrls[i])) {
                    return true;
                }
            }
        }

        //获取请求头token
        AuthenticationToken token = this.createToken(servletRequest, servletResponse);

        if (token.getPrincipal() == null) {
            //handler401(servletResponse, CodeAndMsgEnum.UNAUTHENTIC.getcode(), CodeAndMsgEnum.UNAUTHENTIC.getMsg());
            //return false;
            throw new CMSException(ResultCodeEnum.USER_TOKEN_INCORRECT);
        } else {
            try {
                //shiro登录
                this.getSubject(servletRequest, servletResponse).login(token);
                return true;
            } catch (Exception e) {
                //String msg = e.getMessage();
                ////token错误
                //if (msg.contains("incorrect")) {
                //    //handler401(servletResponse, CodeAndMsgEnum.UNAUTHENTIC.getcode(), msg);
                //    throw new CMSException(ResultCodeEnum.UNAUTHENTIC.getCode(), ResultCodeEnum.UNAUTHENTIC.getMessage());
                //    return false;
                //    //token过期
                //} else if (msg.contains("expired")) {
                //    //尝试刷新token
                //    if (this.refreshToken(servletRequest, servletResponse)) {
                //        return true;
                //    } else {
                //        handler401(servletResponse, CodeAndMsgEnum.UNAUTHENTIC.getcode(), "token已过期,请重新登录");
                //        return false;
                //    }
                //}
                handler401(servletResponse, ResultCodeEnum.USER_TOKEN_EXPIRED);
                return false;
            }
        }
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    private boolean refreshToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取header，tokenStr
        String oldToken = request.getHeader("Authorization");
        String userName = JwtUtil.getUsername(oldToken);
        String key = CommonConstant.JWT_TOKEN + userName;
        //获取redis tokenStr
        String redisUserInfo = (String) redisUtil.get(key);
        if (redisUserInfo != null) {
            if (oldToken.equals(redisUserInfo)) {
                SysUserInfo vo = sysUserInfoMapperPriv.findUserNameByToken(userName);
                //重写生成token(刷新)
                String newTokenStr = JwtUtil.sign(vo.getUserName(), vo.getUserPwd(), Boolean.valueOf(request.getParameter(
                        "rememberMe")));
                JwtToken jwtToken = new JwtToken(newTokenStr);

                //redisUtil.set(userName, newTokenStr);
                SecurityUtils.getSubject().login(jwtToken);
                response.setHeader("Authorization", newTokenStr);
                return true;
            }
        }
        return false;
    }

    /**
     * token有问题
     *
     * @param response
     * @param resultCodeEnum
     */
    private void handler401(ServletResponse response, ResultCodeEnum resultCodeEnum) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.getWriter().write("{\"code\":" + resultCodeEnum.getCode() + ", \"msg\":\"" + resultCodeEnum.getMessage() + "\"}");
        } catch (IOException e) {

            e.printStackTrace();
            throw new CMSException(ResultCodeEnum.USER_TOKEN_INCORRECT);
        }
    }

    /**
     * 支持跨域
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type," +
                "Accept");
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(httpServletRequest, httpServletResponse);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        return new JwtToken(token);
    }
}
