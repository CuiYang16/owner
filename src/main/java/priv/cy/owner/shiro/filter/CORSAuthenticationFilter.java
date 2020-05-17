package priv.cy.owner.shiro.filter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.cy.owner.entity.sysUserInfo.SysUserInfo;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ：cuiyang
 * @description：权限过滤
 * @date ：Created in 2019/12/30 21:17
 */
public class CORSAuthenticationFilter extends FormAuthenticationFilter {

    //加密的字符串,相当于签名
    private static final String SINGNATURE_TOKEN = "加密token";

    private static final Logger logger = LoggerFactory.getLogger(CORSAuthenticationFilter.class);

    public CORSAuthenticationFilter() {
        super();
    }

    @Override
    public boolean isAccessAllowed(
            ServletRequest request, ServletResponse response, Object mappedValue) {
        // Always return true if the request's method is OPTIONS
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                System.out.println("option");
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {

        String token = getRequestToken((HttpServletRequest) request);
        String login = ((HttpServletRequest) request).getServletPath();
        System.out.println("optioddddn");
        logger.debug(token, login);
        //如果为登录,就放行
        if ("/sysuser/login".equals(login)) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            System.out.println("没有token");
            return false;
        }

        //从当前shiro中获得用户信息
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        //对当前ID进行SHA256加密
        String encryptionKey = DigestUtils.sha256Hex(SINGNATURE_TOKEN + user.getUserName());
        logger.debug(encryptionKey);
        if (encryptionKey.equals(token)) {
            return true;
        } else {
            System.out.println("无效token");
        }
        return false;


        //HttpServletResponse res = (HttpServletResponse) response;
        //res.setHeader("Access-Control-Allow-Origin", "*");
        //res.setStatus(HttpServletResponse.SC_OK);
        //res.setCharacterEncoding("UTF-8");
        //PrintWriter writer = res.getWriter();
        //Map<String, Object> map = new HashMap<>();
        //map.put("code", 702);
        //map.put("msg", "未登录");
        //writer.write(JSON.toJSONString(map));
        //writer.close();
        //return false;
    }

    private String getRequestToken(HttpServletRequest request) {
        //默认从请求头中获得token
        String token = request.getHeader("Authorization");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }

}
