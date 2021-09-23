package com.zhoufu.springbootshiromybatis.mapper;

import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.util.MyMapper;
import java.util.List;
import java.util.Map;

public interface ResourcesMapper extends MyMapper<Resources> {
    int insert(Resources record);

    int insertSelective(Resources record);

    int updateByPrimaryKeySelective(Resources record);

    int updateByPrimaryKey(Resources record);

    List<Resources> queryAll();

    List<Resources> loadUserResources(Map<String, Object> map);

    List<Resources> queryResourcesListWithSelected(Integer rid);
}