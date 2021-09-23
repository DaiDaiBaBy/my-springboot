package com.zhoufu.springbootshirodemo.shiro;

import com.zhoufu.springbootshirodemo.model.Menu;
import com.zhoufu.springbootshirodemo.model.User;
import com.zhoufu.springbootshirodemo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    //授权器
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //User user = (User) principals.getPrimaryPrincipal();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println(user.getId());
        //创建一个简单的授权器
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获取用户角色集
        List<String> roleList = userService.getRoleByUserId(user.getId());
        Set<String> set = new HashSet<>(roleList);
        simpleAuthorizationInfo.setRoles(set);

        // 获取用户权限集
        List<Menu> permissionList = userService.getUserMenuAll(user.getId());
        List<String> permissions = new ArrayList<>();
        for (Menu menu: permissionList) {
            // 处理用户多权限 用逗号分隔
            if (StringUtils.isEmpty(menu.getPerms())){
                continue;
            }
            permissions.add(menu.getPerms());
            System.out.println(menu.getPerms());
        }
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }


    //认证器 登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //token 令牌  token.getPrincipal() 获取到前台页面输入的用户名
        // 获取用户输入的用户名和密码
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        User user = userService.getUserByName(userName);
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        //创建一个简单认证器   第一个参数为当前登录用户的主体 可以是用户名 也可以是用户对象 一般都是用户对象
        //第二个参数为数据库查询出来的密码 认证器会用前台传递的密码后查询出来的密码对比
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
        //super.clearCachedAuthorizationInfo(simpleAuthenticationInfo.getPrincipals());
       // SecurityUtils.getSubject().getSession().setAttribute("login",user);
        //MD5 加密+加盐+多次加密
        //SimpleAuthenticationInfo authcInfo = new SimpleAuthenticationInfo(adminUser, password,ByteSource.Util.bytes(salt), this.getName());
        return simpleAuthenticationInfo;
    }
}
