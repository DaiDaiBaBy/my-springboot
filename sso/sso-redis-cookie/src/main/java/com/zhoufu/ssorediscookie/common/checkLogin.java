package com.zhoufu.ssorediscookie.common;

import java.lang.annotation.*;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 15:45
 * @description:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface checkLogin {
    boolean mustLogin() default false;
}
