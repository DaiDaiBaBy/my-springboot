package com.zhoufu.springbootshiromybatis.util;

import com.zhoufu.springbootshiromybatis.model.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 15:27
 * @description:
 */
public class PasswordHelper {
    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void encryptPassword(User user){
        String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getUsername()), hashIterations).toHex();
        user.setPassword(newPassword);
    }

    public static void main(String[] args) {
        PasswordHelper passwordHelper = new PasswordHelper();
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        passwordHelper.encryptPassword(user);
        System.out.println(user);
    }
}
