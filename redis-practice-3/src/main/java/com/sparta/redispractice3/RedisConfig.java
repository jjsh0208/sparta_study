package com.sparta.redispractice3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Integer> articleTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        return template;
    }

}
