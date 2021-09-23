package com.zhoufu.springbootshiromybatis;

import com.zhoufu.springbootshiromybatis.model.Resources;
import com.zhoufu.springbootshiromybatis.service.ResourcesService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringbootShiroMybatisApplicationTests {

	@Test
	void contextLoads() {
	}

	@Resource
	private ResourcesService resourcesService;
	@Test
	public void test(){
		List<Resources> resources = resourcesService.queryAll();
		System.out.println(resources);
	}
}
