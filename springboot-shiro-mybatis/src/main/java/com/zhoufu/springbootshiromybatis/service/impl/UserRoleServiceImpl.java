package com.zhoufu.springbootshiromybatis.service.impl;
import com.zhoufu.springbootshiromybatis.model.UserRole;
import com.zhoufu.springbootshiromybatis.service.UserRoleService;
import com.zhoufu.springbootshiromybatis.shiro.MyShiroRealm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 14:37
 * @description:
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends BaseService<UserRole> implements UserRoleService {

    @Resource
    private MyShiroRealm myShiroRealm;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addUserRole(UserRole userRole) {
        //删除
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid", userRole.getUserid());
        mapper.deleteByExample(example);

        // 添加
        String[] roleIds = userRole.getRoleid().split(",");
        for (String roleId : roleIds) {
            UserRole userRole1 = new UserRole();
            userRole1.setRoleid(roleId);
            userRole1.setUserid(userRole.getUserid());
            mapper.insert(userRole1);
        }

        // 更新当前登录的用户的权限缓存   使用redis 做缓存
        List<Integer> userId = new ArrayList<>();
        userId.add(userRole.getUserid());
        myShiroRealm.clearUserAuthBYUserId(userId);
    }
}
