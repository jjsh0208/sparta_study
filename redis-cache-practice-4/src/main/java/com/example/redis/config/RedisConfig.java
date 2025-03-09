package com.example.redis.config;

import com.example.redis.domain.ItemDto;
import com.example.redis.domain.ItemOrderDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ItemDto> rankTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,ItemDto> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, ItemOrderDto> orderTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,ItemOrderDto> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return redisTemplate;
    }
}
