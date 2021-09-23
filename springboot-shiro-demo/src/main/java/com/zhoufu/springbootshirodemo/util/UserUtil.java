package com.zhoufu.springbootshirodemo.util;

import com.zhoufu.springbootshirodemo.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserUtil {

    public static String getUserId(){
        Subject subject = SecurityUtils.getSubject();

        try {
            User user = (User) subject.getPrincipal();
            if (null != user){
                return user.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("用户不存在");
        }
        return null;
    }

    public static User getUser(){
        Subject subject = SecurityUtils.getSubject();

        try {
            User user = (User) subject.getPrincipal();
            if (null != user){
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("用户不存在");
        }
        return null;
    }


}
