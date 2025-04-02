package com.sprta.gen_ai_sample.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.sprta.gen_ai_sample.consumer_service.WebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Bean
	public WebSocketHandler webSocketHandler() {
		return new WebSocketHandler();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WebSocketHandler(), "/ws/ai")
			.setAllowedOrigins("*"); // 로컬 테스트이므로 모든 origin 허용
	}
}