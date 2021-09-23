package com.zhoufu.springbootshiromybatis.service;

import com.zhoufu.springbootshiromybatis.model.RoleResources;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 16:26
 * @description:
 */
public interface RoleResourcesService extends IService<RoleResources> {
    void addRoleResources(RoleResources roleResources);
}
