package com.zhoufu.ssorediscookie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhoufu.ssorediscookie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 15:37
 * @description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
