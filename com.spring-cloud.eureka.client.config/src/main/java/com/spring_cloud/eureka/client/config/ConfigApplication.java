package com.spring_cloud.eureka.client.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
// spring cloud config server 의 모든 기능을 사용할 수 있도록 설정
// config server 관련 기본 REST API를 자동으로 생성
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

}
