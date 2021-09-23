package com.zhoufu.springbootshiromybatis.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 15:49
 * @description:  通用接口
 */
@Service
public interface IService<T> {

    T selectByKey(Object key);

    int save(T entity);

    int delete(Object key);

    int updateAll(T entity);

    int updateNotNull(T entity);

    List<T> selectByExample(Object example);

    // .....
}
