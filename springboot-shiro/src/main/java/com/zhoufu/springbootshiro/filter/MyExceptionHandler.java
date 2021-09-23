package com.zhoufu.springbootshiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 9:53
 * @description: 注解验证角色和权限的话无法捕捉异常，从而无法正确的返回给前端错误信息，所以这里加一个类用于拦截异常
 */

/**
 * 在Spring里，我们可以使用@ControllerAdvice来声明一些全局性的东西，最常见的是结合@ExceptionHandler注解用于全局异常的处理
 * "@ControllerAdvice是在类上声明的注解，其用法主要有三点：
 * `@ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的；
 * `@InitBinder注解标注的方法：用于请求中注册自定义参数的解析，从而达到自定义请求参数格式的目的；
 *  '@ModelAttribute注解标注的方法：表示此方法会在执行目标Controller方法之前执行
 */
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public String ErrorHandler(AuthorizationException e){
        log.error("没有通过权限验证：{}", e);
        return "my_error";
    }
}
