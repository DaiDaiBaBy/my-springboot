package com.zhoufu.springbootshiromybatis.util;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 15:25
 * @description:
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>{
}
