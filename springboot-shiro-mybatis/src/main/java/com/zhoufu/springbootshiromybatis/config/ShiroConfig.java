package com.zhoufu.springbootshiromybatis.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.service.ResourcesService;
import com.zhoufu.springbootshiromybatis.shiro.MyShiroRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 15:16
 * @description:
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Resource
    private ResourcesService resourcesService;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroDialect 为了在thymeleaf里使用shiro的标签的bean
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    /**
     * ShiroFilterFactoryBean  处理拦截资源文件问题：
     *  注意： 单独一个 ShiroFilterFactoryBean 配置是会报错的， 因为在初始化 ShiroFilterFactoryBean的时候需要注入： SecurityManager
     * @param securityManager
     * @return
     * Filter Chain  定义说明：
     *  1. 一个URL可以配置多个filter， 使用逗号隔开
     *  2. 设置多个过滤器时，全部验证通过，才能通过
     *  3. 部分过滤器可指定参数，如 perms、roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        log.info("Shiro过滤器----shiroFilterFactoryBean");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动查找web工程目录下的“login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功就要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/usersPage");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 配置退出 过滤器， 其中的具体的退出代码 Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout","logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");
        // 过滤器定义，从上到下顺序执行， 一般将/** 放在最为下边，
        // authc: 所有的url 都必须通过验证才能访问，anon： 所有url 都可以匿名访问

        // 自定义加载权限资源关系
        List<Resources> resources = resourcesService.queryAll();
        for (Resources resource : resources) {
            if (StringUtils.isEmpty(resource.getResurl())){
                String permission = "perms[" + resource.getResurl() + "]";
                filterChainDefinitionMap.put(resource.getResurl(), permission);
            }
        }
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 设置 realm
        defaultSecurityManager.setRealm(myShiroRealm());
        // 自定义缓存实现， 使用redis
        defaultSecurityManager.setCacheManager(cacheManager());
        // 自定义session管理， 使用redis
        defaultSecurityManager.setSessionManager(sessionManager());
        return defaultSecurityManager;
    }
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    /**
     * 凭证 匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *   所以我们需要修改下doGetAuthenticationInfo中的代码;
     * @return
     */
    @Bean
    public CredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 散列算法， 这里使用md5算法
        hashedCredentialsMatcher.setHashIterations(2); // 散列的次数， 比如散列两次， 相当于 md5(md5(""))
        return hashedCredentialsMatcher;
    }

    /**
     * 开启 shiro  aop注解支持
     *   使用代理方法，  所以需要开启代码支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置 shiro redisManager
     *  使用的是  shiro-redis 插件
     * @return
     */
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);
        redisManager.setTimeout(timeout);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是  shiro-redis插件
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     *   RedisSessionDAO shiro sessionDao层的实现 通过redis
     *   使用的是shiro-redis开源插件
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
    /**
     * shiro session 管理
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(redisSessionDAO());
        return defaultWebSessionManager;
    }


}
