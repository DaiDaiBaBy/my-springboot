package com.zhoufu.springbootshiromybatis.service;

import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.model.User;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 15:49
 * @description:
 */
public interface UserService extends IService<User> {
    PageInfo<User> selectByPage(User user, int start, int length);

    User selectByUserName(String userName);

    void delUser(Integer userId);
}
