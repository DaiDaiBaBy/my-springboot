package com.zhoufu.springbootshiromybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhoufu.springbootshiromybatis.mapper.RoleMapper;
import com.zhoufu.springbootshiromybatis.mapper.RoleResourcesMapper;
import com.zhoufu.springbootshiromybatis.model.Role;
import com.zhoufu.springbootshiromybatis.model.RoleResources;
import com.zhoufu.springbootshiromybatis.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 14:18
 * @description:
 */
@Service("roleService")
public class RoleServiceImpl extends BaseService<Role> implements RoleService{

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleResourcesMapper roleResourcesMapper;

    @Override
    public List<Role> queryRoleListWithSelected(Integer uid) {
        return roleMapper.queryRoleListWithSelected(uid);
    }

    @Override
    public PageInfo<Role> selectByPage(Role role, int start, int length) {
        int page = start / length + 1;
        Example example = new Example(Role.class);
        PageHelper.startPage(page, length, true);
        List<Role> roleList = selectByExample(example);
        return new PageInfo<>(roleList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void delRole(Integer roleId) {
        // 删除角色
        roleMapper.deleteByPrimaryKey(roleId);
        // 删除角色资源
        Example example = new Example(RoleResources.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleid", roleId);
        roleResourcesMapper.deleteByExample(example);

    }
}
