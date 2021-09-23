package com.zhoufu.ssorediscookie.controller;

import com.alibaba.fastjson.JSON;
import com.zhoufu.ssorediscookie.common.checkLogin;
import com.zhoufu.ssorediscookie.entity.User;
import com.zhoufu.ssorediscookie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 16:08
 * @description:
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/register")
    public String register(User user){
        log.info("注册入口：{}", user.toString());
        Integer register = userService.register(user);
        if (register > 0){
            return "注册成功";
        }
        return "注册失败";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response){
        log.info("登录入口：username=={}，password=={}", username, password);
        User login = userService.login(username, password);
        if (login != null){
            String token = UUID.randomUUID().toString();
            // 把 用户数据保存到redis中
            String jsonString = JSON.toJSONString(login);
            redisTemplate.opsForValue().set(token, jsonString);

            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
            return "登录成功";
        }
        return "登录失败";
    }

    @RequestMapping("/loginOut")
    public String loginOut(@CookieValue(value = "token") Cookie cookie, HttpServletResponse response){
        cookie.setMaxAge(0);
        String token = cookie.getValue();
        if (token != null) {
            redisTemplate.delete(token);
            response.addCookie(cookie);
            return "退出成功！";
        }
        return "退出失败！";
    }

    @RequestMapping("/star")
    @checkLogin(mustLogin = true)
    public String star(){
        return "助力成功";
    }
}
