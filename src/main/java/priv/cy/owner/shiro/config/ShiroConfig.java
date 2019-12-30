package priv.cy.owner.shiro.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import priv.cy.owner.common.jwt.JwtFilter;
import priv.cy.owner.shiro.realm.OwnerRealm;
import priv.cy.owner.shiro.session.CustomSessionManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：cuiyang
 * @description：TODO
 * @date ：Created in 2019/12/30 21:04
 */
@Configuration
public class ShiroConfig {

  @Bean
  public CacheManager cacheManager() {
    return new MemoryConstrainedCacheManager();
  }

  @Bean("shiroFilter")
  public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

    shiroFilterFactoryBean.setSecurityManager(securityManager);

    // 拦截器

    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    // 配置不会被拦截的链接 顺序判断

    filterChainDefinitionMap.put("/login/**", "anon");

    filterChainDefinitionMap.put("/**.js", "anon");

    filterChainDefinitionMap.put("/druid/**", "anon");

    // 添加自己的过滤器并且取名为jwt

    Map<String, Filter> filterMap = new HashMap<String, Filter>(1);

    filterMap.put("jwt", new JwtFilter());

    shiroFilterFactoryBean.setFilters(filterMap);

    // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边

    filterChainDefinitionMap.put("/**", "jwt");

    // 未授权界面;

    shiroFilterFactoryBean.setUnauthorizedUrl("/403");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    return shiroFilterFactoryBean;
  }

  @Bean("securityManager")
  public SecurityManager securityManager(OwnerRealm ownerRealm) {

    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

    securityManager.setRealm(ownerRealm);

    /*
     * 关闭shiro自带的session，详情见文档
     * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%Sessionless%
     */

    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();

    DefaultSessionStorageEvaluator defaultSessionStorageEvaluator =
        new DefaultSessionStorageEvaluator();

    defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

    subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

    securityManager.setSubjectDAO(subjectDAO);

    securityManager.setCacheManager(cacheManager());
    // 注入记住我管理器
    securityManager.setRememberMeManager(rememberMeManager());
    // 注入自定义sessionManager
    securityManager.setSessionManager(sessionManager());

    return securityManager;
  }

  // 自定义sessionManager
  @Bean
  public SessionManager sessionManager() {
    return new CustomSessionManager();
  }

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
  @Bean
  public CookieRememberMeManager rememberMeManager() {
    // System.out.println("ShiroConfiguration.rememberMeManager()");
    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    cookieRememberMeManager.setCookie(rememberMeCookie());
    // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
    cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
    return cookieRememberMeManager;
  }

  /** Shiro生命周期处理器 * @return */
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
}
