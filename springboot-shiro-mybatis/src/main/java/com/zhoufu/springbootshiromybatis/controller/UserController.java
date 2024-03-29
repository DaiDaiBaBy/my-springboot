package com.zhoufu.springbootshiromybatis.controller;

import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.model.User;
import com.zhoufu.springbootshiromybatis.model.UserRole;
import com.zhoufu.springbootshiromybatis.service.UserRoleService;
import com.zhoufu.springbootshiromybatis.service.UserService;
import com.zhoufu.springbootshiromybatis.util.PasswordHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 15:32
 * @description:
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;

    @RequestMapping
    public Map<String,Object> getAll(User user, String draw,
                                     @RequestParam(required = false, defaultValue = "1") int start,
                                     @RequestParam(required = false, defaultValue = "10") int length){
        Map<String,Object> map = new HashMap<>();
        PageInfo<User> pageInfo = userService.selectByPage(user, start, length);
        System.out.println("pageInfo.getTotal():"+pageInfo.getTotal());
        map.put("draw",draw);
        map.put("recordsTotal",pageInfo.getTotal());
        map.put("recordsFiltered",pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 保存用户角色 信息
     * @param userRole 用户角色
     *  	  此处获取的参数的角色id是以 “,” 分隔的字符串
     * @return
     */
    @RequestMapping("/saveUserRoles")
    public String saveUserRoles(UserRole userRole){
        if(StringUtils.isEmpty(userRole.getUserid()))
            return "error";
        try {
            userRoleService.addUserRole(userRole);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // 添加用户
    @RequestMapping(value = "/add")
    public String add(User user) {
        User u = userService.selectByUserName(user.getUsername());
        if(u != null)
            return "error";
        try {
            user.setEnable(1);
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            userService.save(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(Integer id){
        try{
            userService.delUser(id);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

}
