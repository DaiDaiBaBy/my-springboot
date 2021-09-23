package com.zhoufu.springbootshiromybatis.service;

import com.zhoufu.springbootshiromybatis.model.UserRole;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 16:20
 * @description:
 */
public interface UserRoleService extends IService<UserRole> {
    void addUserRole(UserRole userRole);
}
