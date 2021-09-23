package com.zhoufu.springbootshiro.service.impl;

import com.zhoufu.springbootshiro.bean.Permissions;
import com.zhoufu.springbootshiro.bean.Role;
import com.zhoufu.springbootshiro.bean.User;
import com.zhoufu.springbootshiro.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 10:27
 * @description:
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public User getUserByName(String userName) {
        return getMapByName(userName);
    }

    /**
     * 模拟数据库查询
     * @param userName 用户名
     * @return
     */
    private User getMapByName(String userName) {
        Permissions query = new Permissions("1", "query");
        Permissions add = new Permissions("2", "add");
        Set<Permissions> permissionsSet = new HashSet<>();
        permissionsSet.add(query);
        permissionsSet.add(add);

        Role role_admin = new Role("1", "admin", permissionsSet);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role_admin);

        User user = new User("1", "zhangsan", "123456", roleSet);

        Map<String, User> map = new HashMap<>();
        map.put(user.getUserName(), user);

        Set<Permissions> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(query);
        Role role_user = new Role("2", "user", permissionsSet1);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role_user);
        User wangwu = new User("2", "wangwu", "123456", roleSet1);
        map.put(wangwu.getUserName(), wangwu);
        return map.get(userName);

    }
}
