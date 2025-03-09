package com.example.redis.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching // 캐싱 활성화
public class CacheConfig {
    // 캐시 매니저 구현체를 등록해줘야한다

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        // 설정 구성을 먼저 진행
        // redis를 이용해서 spring cache를 사용할 때 redis 관련 설정을 모아두는 클래스

        // defaultCacheConfig 기본 설정
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                // null을 캐싱하지않겠다는 의미
                .disableCachingNullValues()
                // 기본 캐시 유지 시간 (Time to Live) 10초
                .entryTtl(Duration.ofSeconds(120))
                // 캐시를 구분하는 접두사 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할것인지
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));


        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration)

                .build();
    }
}
