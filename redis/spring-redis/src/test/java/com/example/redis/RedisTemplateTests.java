package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void stringOpsTest(){

        // 문자열 조작을 위한 클래스
        ValueOperations<String, String> ops
                // 지금 RedisTemplate 에 설정된 타입을 바탕으로 Redis 문자열 조직을 할거다
                = stringRedisTemplate.opsForValue();

        ops.set("simplekey","simplevalue");
        System.out.println("ops.get(\"simplekey\") = " + ops.get("simplekey"));

        // 집합( set ) 을 조작하기 위한 클래스
        SetOperations<String,String> setOps
                = stringRedisTemplate.opsForSet();


        setOps.add("bobbies","games");
        setOps.add("bobbies",
                "coding" , "alcohol","games");

        System.out.println("setOps.size(\"bobbies\") = " + setOps.size("bobbies"));

        stringRedisTemplate.expire("bobbies",10, TimeUnit.SECONDS);
        stringRedisTemplate.delete("simplekey");

    }


    @Autowired
    private RedisTemplate<String ,ItemDto> itemRedisTemplate;

    @Test
    public void itemRedisTemplateTest(){
        ValueOperations<String, ItemDto> ops
                = itemRedisTemplate.opsForValue();


        ops.set("my:keyboard", ItemDto.builder()
                        .name("mechanical keyboard")
                        .price(10000)
                        .description("omg")
                        .build());
        System.out.println("ops.get(\"my:keyboard\") = " + ops.get("my:keyboard"));

        ops.set("my:mouse", ItemDto.builder()
                        .name("mouse mice")
                        .price(20000)
                        .description("OMG")
                        .build());
        System.out.println("ops.get(\"my:mouse\").getDescription() = " + ops.get("my:mouse").getDescription());
    }

}
