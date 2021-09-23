package com.zhoufu.ssorediscookie.service;

import com.zhoufu.ssorediscookie.entity.User;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 15:38
 * @description:
 */
public interface UserService {
    User login(String username, String password);

    Integer register(User user);
}
