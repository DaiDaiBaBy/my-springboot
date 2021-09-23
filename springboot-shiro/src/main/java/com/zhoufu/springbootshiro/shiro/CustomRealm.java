package com.zhoufu.springbootshiro.shiro;

import com.zhoufu.springbootshiro.bean.Permissions;
import com.zhoufu.springbootshiro.bean.Role;
import com.zhoufu.springbootshiro.bean.User;
import com.zhoufu.springbootshiro.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 9:49
 * @description: 自定义Realm 用于查询用户的角色和权限信息并保存到权限管理器
 */
// 权限配置类
public class CustomRealm extends AuthorizingRealm{
    @Autowired
    private LoginService loginService;

    /**
     * 权限配置
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 查询用户名称
        User user = loginService.getUserByName(userName);
        // 添加权限和角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            // 添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            // 添加权限
            for (Permissions permissions : role.getPermissions()) {
                simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 认证 配置
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())){
            return null;
        }
        // 获取用户信息
        String name= (String) authenticationToken.getPrincipal().toString();
        User userByName = loginService.getUserByName(name);
        if (userByName == null){
            // 返回报出对应异常
            return null;
        } else {
            // 这里验证 authorizationToken 和 simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, userByName.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
