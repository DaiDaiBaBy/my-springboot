package com.zhoufu.springbootshiromybatis.shiro;

import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.model.User;
import com.zhoufu.springbootshiromybatis.service.ResourcesService;
import com.zhoufu.springbootshiromybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 16:55
 * @description:
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Resource
    private ResourcesService resourcesService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        List<Resources> resources = resourcesService.loadUserResources(map);
        // 权限信息对象 info ， 用来存放查出的用户的所有角色role 以及权限 permission
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Resources resource : resources) {
            info.addStringPermission(resource.getResurl());
        }
        return info;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的账号
        String userName = (String) token.getPrincipal();
        User user = userService.selectByUserName(userName);
        if (null == user) throw new UnknownAccountException();
        if (0 == user.getEnable()){
            throw new LockedAccountException(); // 账号锁定
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(userName), getName());
        // 当验证都通过， 则把 用户信息放到 session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSession", user);
        session.setAttribute("userSessionId", user.getId());
        return simpleAuthenticationInfo;
    }

    /**
     * 根据UserId 清除当前的session存在的用户权限缓存
     * @param userIds 已经修改了权限的userid
     */
    public void clearUserAuthBYUserId(List<Integer> userIds){
        if (null == userIds || userIds.size() == 0);
        //  获取所有的session
        Collection<Session> activeSessions = redisSessionDAO.getActiveSessions();
        //  定义返回
        List<SimplePrincipalCollection> list = new ArrayList<>();
        for (Session activeSession : activeSessions) {
            //  获取session登录信息
            Object attribute = activeSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null != attribute && attribute instanceof SimplePrincipalCollection){
                // 强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) attribute;
                // 判断用户，  匹配用户ID
                attribute = spc.getPrimaryPrincipal();
                if (null != attribute && attribute instanceof User){
                    User user = (User) attribute;
                    log.info("匹配用户user== {}", user);
                    //  比较用户ID， 符合即加入集合
                    if (null != userIds && userIds.contains(user.getId())){
                        list.add(spc);
                    }
                }
            }
        }
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm) securityManager.getRealms().iterator().next();
        for (SimplePrincipalCollection simplePrincipalCollection : list) {
            realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
        }
    }
}
