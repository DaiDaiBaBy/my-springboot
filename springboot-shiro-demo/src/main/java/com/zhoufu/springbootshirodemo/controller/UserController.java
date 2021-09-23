package com.zhoufu.springbootshirodemo.controller;
import com.zhoufu.springbootshirodemo.model.Menu;
import com.zhoufu.springbootshirodemo.model.User;
import com.zhoufu.springbootshirodemo.service.UserService;
import com.zhoufu.springbootshirodemo.util.UserUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("user/toUserList")
    public String toUserList(){
        return "userList";
    }

    @RequestMapping("user/getUserMenu")
    @ResponseBody
    public List<Menu> getUserMenu(){
        String userId = UserUtil.getUserId();
        return userService.getUserMenu(userId);
    }

    @RequestMapping("user/getUser")
    @ResponseBody
    @RequiresPermissions("user:list")//shiro授权基于aop实现
    public List<User> getUser(){
        List<User> userList = new ArrayList<>();
        try {
            userList = userService.getUser();
        }catch(AuthorizationException e){
            e.printStackTrace();
        }
        return userList;
    }
}
