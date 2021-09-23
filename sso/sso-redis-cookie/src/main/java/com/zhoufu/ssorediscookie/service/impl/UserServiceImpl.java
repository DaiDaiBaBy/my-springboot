package com.zhoufu.ssorediscookie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhoufu.ssorediscookie.entity.User;
import com.zhoufu.ssorediscookie.mapper.UserMapper;
import com.zhoufu.ssorediscookie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 15:39
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if (user != null){
            if (user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    @Override
    public Integer register(User user) {
        return userMapper.insert(user);
    }
}
