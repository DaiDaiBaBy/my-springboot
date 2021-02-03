package com.zhoufu.springbootexcel;

import com.zhoufu.springbootexcel.mapper.GoodsMapper;
import com.zhoufu.springbootexcel.model.Goods;
import com.zhoufu.springbootexcel.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootExcelApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private GoodsMapper goodsMapper;
	@Test
	void test(){
		List<Goods> list = goodsMapper.queryAllGoods();
		System.out.println("结果：" + JsonUtils.beanToJson(list));
	}

}
