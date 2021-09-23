package com.zhoufu.springbootshiromybatis.mapper;

import com.zhoufu.springbootshiromybatis.model.RoleResources;
import com.zhoufu.springbootshiromybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface RoleResourcesMapper extends MyMapper<RoleResources> {

    int insert(RoleResources record);

    int insertSelective(RoleResources record);
}