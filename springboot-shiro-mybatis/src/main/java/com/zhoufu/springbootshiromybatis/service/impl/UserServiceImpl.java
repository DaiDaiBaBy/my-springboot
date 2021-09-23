package com.zhoufu.springbootshiromybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.mapper.UserMapper;
import com.zhoufu.springbootshiromybatis.mapper.UserRoleMapper;
import com.zhoufu.springbootshiromybatis.model.User;
import com.zhoufu.springbootshiromybatis.model.UserRole;
import com.zhoufu.springbootshiromybatis.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 9:40
 * @description:
 */
@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Override
    public PageInfo<User> selectByPage(User user, int start, int length) {
        int page = start / length + 1;
        /*  运算符：
         *  /  :  除法运算符 取最大整数值（向下取整） 7/3 = 2    3/2 = 1  1/3 = 0
         *  %  :  模运算：
         *      (1) 当左边小于右边，结果就等于左边   2%3 = 2    1%3 = 1
         *      (2) 左边大于右边， 则就进行取余运算   3%2 = 1   6%3 = 0   7%3 = 1
         */
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(user.getUsername())){
            criteria.andLike("username", "%" + user.getUsername() + "%");
        }
        if (user.getId() != null){
            criteria.andEqualTo("id", user.getId());
        }
        if (user.getEnable() != null){
            criteria.andEqualTo("enable", user.getEnable());
        }
        // 进行分页查询
        PageHelper.startPage(page, length, true);
        List<User> userList = selectByExample(example);
        return new PageInfo<>(userList);
    }

    @Override
    public User selectByUserName(String userName) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        List<User> userList = selectByExample(example);
        if (userList.size() > 0){
            return userList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void delUser(Integer userId) {
        // 删除用户表
        mapper.deleteByPrimaryKey(userId);
        // 删除用户角色表
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid", userId);
        userRoleMapper.deleteByExample(example);
    }
}
