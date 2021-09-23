package com.zhoufu.ssoauthserver.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zhoufu
 * @Date: 2021/2/23 14:27
 * @description:
 */
public class CookieUtil {
    public static String getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String value, int maxAge){
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
