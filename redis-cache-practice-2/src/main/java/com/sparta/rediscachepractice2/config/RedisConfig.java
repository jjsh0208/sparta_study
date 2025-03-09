package com.sparta.rediscachepractice2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//
//
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(RedisSerializer.json());
//
//
//        return redisTemplate;
//    }
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
//        return RedisSerializer.json();
//    }


    // Security Context를 직접 직렬화 하지 않는 한 json의 형태로 가공하기는 힘들다.
    //아니면 직접 context를 사용할 수 있게 필터를 만들면 상관없다?
}
