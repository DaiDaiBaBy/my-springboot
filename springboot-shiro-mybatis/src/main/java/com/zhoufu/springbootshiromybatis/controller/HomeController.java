package com.zhoufu.springbootshiromybatis.controller;

import com.zhoufu.springbootshiromybatis.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 15:07
 * @description:
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, User user, Model model){
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())){
            request.setAttribute("msg", "用户名或密码不能为空");
            return "login";
        }

        // 权限验证
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            return "redirect:usersPage";
        } catch (LockedAccountException e){
            token.clear();
            request.setAttribute("msg", "用户已经被锁定不能登录， 请联系管理员处理");
            return "login";
        } catch (AuthenticationException e){
            token.clear();
            request.setAttribute("msg", "用户名或密码不正确");
            return "login";
        } catch (AuthorizationException e){
            token.clear();
            request.setAttribute("msg", "没有权限");
            return "login";
        }
    }

    @RequestMapping(value = {"/usersPage", ""})
    public String usersPage(){
        return "user/users";
    }
    @RequestMapping("/rolesPage")
    public String rolesPage(){
        return "role/roles";
    }

    @RequestMapping("/resourcesPage")
    public String resourcesPage(){
        return "resources/resources";
    }

    @RequestMapping("/403")
    public String forbidden(){
        return "403";
    }
}
