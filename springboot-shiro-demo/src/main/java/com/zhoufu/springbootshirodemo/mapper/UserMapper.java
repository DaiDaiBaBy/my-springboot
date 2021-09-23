package com.zhoufu.springbootshirodemo.mapper;

import com.zhoufu.springbootshirodemo.model.Menu;
import com.zhoufu.springbootshirodemo.model.User;

import java.util.List;

public interface UserMapper {

    User getUserByName(String userName);

    List<User> getUser();

    List<Menu> getUserMenu(String userId);

    List<String> getRoleByUserId(String id);

    List<Menu> getUserMenuAll(String id);
}
