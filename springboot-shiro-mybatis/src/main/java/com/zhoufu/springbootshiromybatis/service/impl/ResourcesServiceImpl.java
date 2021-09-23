package com.zhoufu.springbootshiromybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.mapper.ResourcesMapper;
import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.service.ResourcesService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 14:51
 * @description:
 */
@Service("resourcesService")
public class ResourcesServiceImpl extends BaseService<Resources> implements ResourcesService {
    @Resource
    private ResourcesMapper resourcesMapper;

    @Override
    public PageInfo<Resources> selectByPage(Resources resources, int start, int length) {
        int page = start / length + 1;
        Example example = new Example(Resources.class);
        PageHelper.startPage(page, length, true);
        List<Resources> resourcesList = selectByExample(example);
        return new PageInfo<>(resourcesList);
    }

    @Override
    public List<Resources> queryAll() {
        return resourcesMapper.queryAll();
    }

    @Override
    public List<Resources> loadUserResources(Map<String, Object> map) {
        return resourcesMapper.loadUserResources(map);
    }

    @Override
    public List<Resources> queryResourcesListWithSelected(Integer id) {
        return queryResourcesListWithSelected(id);
    }
}
