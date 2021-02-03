package com.zhoufu.springbootaccesscontrol;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhoufu
 * @Date: 2021/2/3 16:23
 * @description: 拦截器实现
 */
@Slf4j
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            //获取方法中的注解,看是否有该注解
            AccessLimit methodAnnotation = handlerMethod.getMethod().getAnnotation(AccessLimit.class);
            // 获取类 中是否包含注解
            AccessLimit classAnnotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(AccessLimit.class);
            // 如果方法上有注解  则优先方法上的
            AccessLimit accessLimit = methodAnnotation != null?methodAnnotation:classAnnotation;
            if (accessLimit == null){
                return true;
            }
            if (isLimit(request, accessLimit)){
                render(response, Result.error(ResultEnum.REQUST_LIMIT));
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 判断 请求是否受限制
     * @param request
     * @param accessLimit
     * @return
     */
    private boolean isLimit(HttpServletRequest request, AccessLimit accessLimit) {
        /**
         * redis 缓存 受限制的key
         * 1.   倘若 是浏览器  就使用sessionId作为唯一key
         * 2.   如果是app， 则可使用用户的唯一id  userId 作为key
         */
        //  这里模拟浏览器， 获取登录的 session 进行判断
        // ......
//            requestURI = ""+"1";  // 这里假设用户是1,项目中是动态获取的userId
        String limitKey = request.getRequestURI() + request.getSession().getId();
        log.info("所存redis-key：{}", limitKey);
        // 从redis缓存中获取， 当前这个请求访问了几次
        Integer redisCount = (Integer) redisTemplate.opsForValue().get(limitKey);
        log.info("redis-count:{}", redisCount);
        if (redisCount == null) {
            // 第一次
            redisTemplate.opsForValue().set(limitKey, 1, accessLimit.second(), TimeUnit.SECONDS);
        } else {
            if (redisCount >= accessLimit.maxCount()) {
                return true;
            }
            // 次数自增
            redisTemplate.opsForValue().increment(limitKey);
        }
        return false;
    }

    private void render(HttpServletResponse response, Result result) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = null;
        String json = JSONObject.toJSONString(result);
        out = response.getWriter();
        out.append(json);
    }

}
