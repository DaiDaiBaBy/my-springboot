package com.zhoufu.springbootshiromybatis.service.impl;

import com.zhoufu.springbootshiromybatis.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 9:37
 * @description:
 */
public abstract class BaseService<T> implements IService<T> {

    @Autowired
    protected Mapper<T> mapper;

    public Mapper<T> getMapper(){
        return mapper;
    }

    @Override
    public T selectByKey(Object key) {
        return null;
    }

    @Override
    public int save(T entity) {
        return 0;
    }

    @Override
    public int delete(Object key) {
        return 0;
    }

    @Override
    public int updateAll(T entity) {
        return 0;
    }

    @Override
    public int updateNotNull(T entity) {
        return 0;
    }

    @Override
    public List<T> selectByExample(Object example) {
        return null;
    }
}
