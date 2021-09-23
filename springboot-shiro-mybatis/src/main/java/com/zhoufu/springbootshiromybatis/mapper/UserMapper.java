package com.zhoufu.springbootshiromybatis.mapper;

import com.zhoufu.springbootshiromybatis.model.User;
import com.zhoufu.springbootshiromybatis.util.MyMapper;

public interface UserMapper extends MyMapper<User>{

    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}