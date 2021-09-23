package com.zhoufu.springbootshirodemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.zhoufu.springbootshirodemo.mapper")
@ComponentScan("com.zhoufu.springbootshirodemo.service")
public class SpringbootShiroDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootShiroDemoApplication.class, args);
	}

}
