package com.zhoufu.springbootshiromybatis.service;

import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.model.Resources;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 16:21
 * @description:
 */
public interface ResourcesService extends IService<Resources> {
    PageInfo<Resources> selectByPage(Resources resources, int start, int length);

    List<Resources> queryAll();

    List<Resources> loadUserResources(Map<String, Object> map);

    List<Resources> queryResourcesListWithSelected(Integer id);
}
