package com.zhoufu.springbootshiromybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.zhoufu.springbootshiromybatis.mapper")
public class SpringbootShiroMybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootShiroMybatisApplication.class, args);
	}

}
