package com.sparta.redispractice3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * -- 5. 실제 Entity 등은 만들지 안혹, Redis에 데이터만 저장
 * --  Redis의 문자열은 저장된 데이터가 정수라면
 * --  INCR, DECR 등으로 값을 쉽게 조정할 수 있다.
 * --  추가로 존재하지 않는 데이터에 대해서 실행할 경우 0으로 초기화된다.
 * --  INCR articles:{id}
 *
 * --  만약 날짜가 바뀔 때 데이터를 저장하고 싶다면,
 * -- Key를 articles:{id}:today 등으로 만들고
 * -- INCR articles:{id}:today
 *
 * -- 날짜가 바뀌는 시점에 RENAME으로 해당 날짜를 기록하면 된다.
 * -- RENAME articles:{id}:today articles:{id}:20xx-xx-xx
 */


@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ValueOperations<String, Integer> ops;

    public ArticleController(RedisTemplate<String, Integer> articlesTemplate) {
        this.ops = articlesTemplate.opsForValue();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void read(@PathVariable("id") Long id){
        ops.increment("articles:%d".formatted(id));
    }

}

