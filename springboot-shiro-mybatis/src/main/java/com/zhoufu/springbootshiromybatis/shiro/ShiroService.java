package com.zhoufu.springbootshiromybatis.shiro;

import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 17:36
 * @description:
 */
@Slf4j
@Service
public class ShiroService {
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Autowired
    private ResourcesService resourcesService;

    /**
     * 初始化 权限
     * @return
     */
    public Map<String, String> loadFilterChainDefinitions(){
        // 权限控制map   从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");
        List<Resources> resources = resourcesService.queryAll();
        for (Resources resource : resources) {
            if (StringUtils.isEmpty(resource.getResurl())){
                String permission = "perms[" + resource.getResurl() + "]";
                filterChainDefinitionMap.put(resource.getResurl(), permission);
            }
        }
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission(){
        synchronized (shiroFilterFactoryBean){
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空 老的权限控制
            filterChainManager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());

            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                filterChainManager.createChain(url, chainDefinition);
            }
            log.info("更新权限成功！");
        }
    }
}
