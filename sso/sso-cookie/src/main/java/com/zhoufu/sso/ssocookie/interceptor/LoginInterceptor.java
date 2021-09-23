package com.zhoufu.sso.ssocookie.interceptor;

import com.zhoufu.sso.ssocookie.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 13:43
 * @description: 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor{
    private RedisTemplate<String, User> redisTemplate;
    // 实例化时 初始化redisTemplate
    public LoginInterceptor(RedisTemplate<String, User> redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 获取session， 模拟本地session登录
        HttpSession session = request.getSession();
        String token = null;
        Cookie[] cookies = request.getCookies();
        Cookie respCookie = null;
        // 没有cookie 则说明没有登录
        if (cookies == null){
            response.sendRedirect("/login");
            return false;
        }
        for (Cookie cookie : cookies) {
            // 拿到 cookie里面的 token
            if ("token".equals(cookie.getName())){
                respCookie = cookie;
                token = cookie.getValue();
            }
        }
        // token 空这说明 没有登录
        if (token != null){
            // 根据 token 从redis里面获取到用户
            try {
                User user = redisTemplate.opsForValue().get(token);
                // 已经登录(user 取值没问题的话)
                // 设置个 session 方便取值
                session.setAttribute("user", user);
                return true;
            } catch (Exception e){
                // 在redis里面没有匹配的数据， 则没有登录
                respCookie.setMaxAge(0);
                respCookie.setDomain("baidu.com");
                // 响应到浏览器
                response.addCookie(respCookie);
                // 回到登录页面
                response.sendRedirect("/login");
                return false;
            }
        } else {
            response.sendRedirect("/login");
            return false;
        }
    }
}
