package com.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,ItemDto> itemRedisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, ItemDto> redisTemplate
                = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 키를 어떻게 직렬화 역직렬화 할것인지
        redisTemplate.setKeySerializer(RedisSerializer.string());

        // 벨류를 어떻게 직렬화 역직렬화 할것인지
        redisTemplate.setValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }



}
