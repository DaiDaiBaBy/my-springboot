package com.zhoufu.springbootshirodemo.service;

import com.zhoufu.springbootshirodemo.model.Menu;
import com.zhoufu.springbootshirodemo.model.User;

import java.util.List;

public interface UserService {

    User getUserByName(String userName);

    List<User> getUser();

    List<Menu> getUserMenu(String userId);

    List<String> getRoleByUserId(String id);

    List<Menu> getUserMenuAll(String id);
}
