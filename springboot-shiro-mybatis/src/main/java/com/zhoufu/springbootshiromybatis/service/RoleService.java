package com.zhoufu.springbootshiromybatis.service;

import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.model.Role;

import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 16:02
 * @description:
 */
public interface RoleService extends IService<Role> {
    List<Role> queryRoleListWithSelected(Integer uid);

    PageInfo<Role> selectByPage(Role role, int start, int length);

    /**
     * 删除角色  同时删除角色资源表中的数据
     */
    void delRole(Integer roleId);
}
