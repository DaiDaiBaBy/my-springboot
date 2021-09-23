package com.zhoufu.springbootshiro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 9:45
 * @description:  角色
 */
@Data
@AllArgsConstructor
public class Role {
    private String id;
    private String roleName;

    /**
     * 角色对应权限集合
     */
    private Set<Permissions> permissions;
}
