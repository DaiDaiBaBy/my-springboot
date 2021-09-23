package com.zhoufu.springbootshiro.service;

import com.zhoufu.springbootshiro.bean.User;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 10:26
 * @description:
 */
public interface LoginService {
    User getUserByName(String userName);
}
