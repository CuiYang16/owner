package priv.cy.owner.shiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import priv.cy.owner.common.jwt.JWTFilter;
import priv.cy.owner.config.redis.RedisConfig;
import priv.cy.owner.shiro.cache.ShiroCacheManager;
import priv.cy.owner.shiro.realm.OwnerRealm;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 21:04
 */
@SuppressWarnings("ALL")
@Configuration
public class ShiroConfig {

    //@Bean
    //public CacheManager cacheManager() {
    //    return new ShiroCacheManager();
    //}


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        // shiroFilterFactoryBean.setLoginUrl("/sysuser/login");
        // 设置成功跳转的页面
        // shiroFilterFactoryBean.setSuccessUrl("/index");
        // 设置无权限时跳转的 url;
        // shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
        // 拦截器

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 配置不会被拦截的链接 顺序判断

        filterChainDefinitionMap.put("/sysuser/login", "anon");
        filterChainDefinitionMap.put("/**", "anon");
        filterChainDefinitionMap.put("/**.js", "anon");
        //管理员，需要角色权限 “admin”
        //filterChainDefinitionMap.put("/sysuser/users", "admin");

        filterChainDefinitionMap.put("/druid/**", "anon");

        // 添加自己的过滤器并且取名为jwt

        Map<String, Filter> filterMap = new HashMap<String, Filter>();

        filterMap.put("jwt", new JWTFilter());

        shiroFilterFactoryBean.setFilters(filterMap);

        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边

        filterChainDefinitionMap.put("/**", "jwt");

        // 未授权界面;

        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setLoginUrl("/sysuser/unauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 密码校验规则HashedCredentialsMatcher，也就是密码比对器 这个类是为了对密码进行编码的 , 防止密码在数据库里明码保存 , 当然在登陆认证的时候 ,
     * 这个类也负责对form里输入的密码进行编码 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
     */
    @Bean("credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        // 加密次数
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    // 自定义sessionManager
    @Bean
    public DefaultWebSecurityManager securityManager(OwnerRealm shiroRealm, ShiroCacheManager shiroCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        //自定义缓存管理
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setJedisPool(RedisConfig.getJedisPoolInstance());

        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    //@Bean("redisSessionManager")
    //public SessionManager redisSessionManager() {
    //    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    //    Collection<SessionListener> listeners = new ArrayList<SessionListener>();
    //    //配置监听
    //    listeners.add(sessionListener());
    //    sessionManager.setSessionListeners(listeners);
    //    sessionManager.setSessionIdCookie(sessionIdCookie());
    //    sessionManager.setSessionDAO(redisSessionDAO());
    //    sessionManager.setCacheManager(cacheManager());
    //    //sessionManager.setGlobalSessionTimeout(60000);    //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
    //    sessionManager.setDeleteInvalidSessions(true);
    //    //取消url 后面的 JSESSIONID
    //    sessionManager.setSessionIdUrlRewritingEnabled(false);
    //    return sessionManager;
    //
    //}

    /**
     * cookie对象; rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // System.out.println("ShiroConfiguration.rememberMeCookie()");
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象; rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     *
     * @return
     */
    @SuppressWarnings("AlibabaRemoveCommentedCode")
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        // System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    /**
     * Shiro生命周期处理器 * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean(name = "jwtFilter")
    public JWTFilter jwtFilter() {
        return new JWTFilter();
    }


}
