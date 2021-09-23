package com.zhoufu.ssorediscookie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhoufu.ssorediscookie.mapper.UserMapper")
public class SsoRedisCookieApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoRedisCookieApplication.class, args);
	}

}
