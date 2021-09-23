package com.zhoufu.sso.ssocookie.controller;

import com.zhoufu.sso.ssocookie.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 14:09
 * @description:
 */
@Controller
public class LoginController {
    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(@CookieValue(value = "token", required = false) Cookie cookie){
        // if cookie不为空 这说明已经登录 ，跳转至主页面即可
        if (cookie != null){
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 处理登录
     * @param user
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(User user, HttpServletResponse response, Model model){
        // 验证 用户 模拟登录
        if (user != null){
            // 用户名 密码
            if ("zhoufu".equals(user.getUname()) && "123".equals(user.getUpassword())){
                // 生成token
                String token = UUID.randomUUID().toString();

                // 创建 cookie
                Cookie cookie = new Cookie("token", token);

                // 设置 域名
                cookie.setDomain("baidu.com");

                //写入redis
                redisTemplate.opsForValue().set(token, user);

                // 将cookie响应给浏览器
                response.addCookie(cookie);

                return "redirect:/index";
            } else {
                model.addAttribute("msg", "账号或者密码错误");
                return "login";
            }
        } else {
            model.addAttribute("msg", "账号或者密码错误");
            return "login";
        }
    }

    /**
     * login our
     * @param cookie
     * @param response
     * @return
     */
    @RequestMapping("loginOut")
    public String loginOut(@CookieValue(value = "token") Cookie cookie, HttpServletResponse response){
        // 设置 域
        cookie.setDomain("baidu.com");
        // 设置过期时间
        cookie.setMaxAge(0);
        // 拿到 token
        String token = cookie.getValue();
        // 根据token 从redis中删除
        redisTemplate.delete(token);
        // 响应token到浏览器
        response.addCookie(cookie);
        // 返回登录页面
        return "redirect:/login";
    }
}
