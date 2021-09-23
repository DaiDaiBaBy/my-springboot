package com.zhoufu.springbootshiromybatis.mapper;

import com.zhoufu.springbootshiromybatis.model.UserRole;
import com.zhoufu.springbootshiromybatis.util.MyMapper;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {
    int insert(UserRole record);

    int insertSelective(UserRole record);

    List<Integer> findUserIdByRoleId(Integer roleId);
}