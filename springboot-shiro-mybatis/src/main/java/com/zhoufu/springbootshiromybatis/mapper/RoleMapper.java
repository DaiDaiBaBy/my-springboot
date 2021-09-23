package com.zhoufu.springbootshiromybatis.mapper;

import com.zhoufu.springbootshiromybatis.model.Role;
import com.zhoufu.springbootshiromybatis.util.MyMapper;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    int insert(Role record);

    int insertSelective(Role record);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> queryRoleListWithSelected(Integer id);
}