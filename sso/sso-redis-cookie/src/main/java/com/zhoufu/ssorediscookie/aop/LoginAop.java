package com.zhoufu.ssorediscookie.aop;

import com.alibaba.fastjson.JSON;
import com.zhoufu.ssorediscookie.common.checkLogin;
import com.zhoufu.ssorediscookie.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 15:48
 * @description:
 */
@Aspect
@Component
@Slf4j
public class LoginAop {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 登录验证
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.zhoufu.ssorediscookie.common.checkLogin)")
    public String isLogin(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        checkLogin annotation = method.getAnnotation(checkLogin.class);
        // 获取request实例
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
//        HttpServletRequest request = sra.getRequest();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    token = cookie.getValue();
                }
            }
        }
        String result = null;
        if (!StringUtils.isEmpty(token)){
            result = redisTemplate.opsForValue().get(token);
        }
        // 已经登录
        if (result != null){
            User user = JSON.parseObject(result, User.class);
            log.info("已经登录：{}", user);
        } else {
            // 没有登录
            if (annotation.mustLogin()){
                // 必须登录
                return "请先登录！";
            }
        }

        try {
            return (String) joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }
}
