package com.zhoufu.springbootshiromybatis.service.impl;

import com.zhoufu.springbootshiromybatis.mapper.UserRoleMapper;
import com.zhoufu.springbootshiromybatis.model.RoleResources;
import com.zhoufu.springbootshiromybatis.service.RoleResourcesService;
import com.zhoufu.springbootshiromybatis.shiro.MyShiroRealm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/3/2 14:55
 * @description:
 */
@Service("roleResourcesService")
public class RoleResourcesServiceImpl extends BaseService<RoleResources> implements RoleResourcesService {
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private MyShiroRealm myShiroRealm;

    // 更新权限
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addRoleResources(RoleResources roleResources) {
        // 删除
        Example example = new Example(RoleResources.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleid", roleResources.getRoleid());
        mapper.deleteByExample(example);

        // 添加
        if (!StringUtils.isEmpty(roleResources.getResourcesid())){
            String[] split = roleResources.getResourcesid().split(",");
            for (String s : split) {
                RoleResources roleResources1 = new RoleResources();
                roleResources1.setResourcesid(s);
                roleResources1.setRoleid(roleResources.getRoleid());
                mapper.insert(roleResources1);
            }
        }
        // 更新当前登录用户的权限缓存  redis
        List<Integer> userIdByRoleId = userRoleMapper.findUserIdByRoleId(roleResources.getRoleid());
        myShiroRealm.clearUserAuthBYUserId(userIdByRoleId);
    }
}
