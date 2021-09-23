package com.zhoufu.springbootshiro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 9:45
 * @description:
 */
@Data
@AllArgsConstructor
public class User {
    private String id;
    private String userName;
    private String password;

    /**
     * 用户对应的角色集合
     */
    private Set<Role> roles;

}
