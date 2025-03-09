package com.sparta.redispractice1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;


import java.util.Set;


@RestController
@RequiredArgsConstructor
public class ArticlesController {

    private final RedisTemplate<String, Object> redisTemplate;


    /**
     *
     * -- 1. 내 블로그 별 조회수를 Redis 로 확인하고 싶다.
     * --   1. 블로그 url의 path는 '/articles/{id}' 형식이다.
     * --   2. 로그인 여부와 상관없이 새로고침 될때마다 조회수가 하나 증가한다.
     * --   3. 이를 관리하기 위해 적당한 데이터 타입을 선정하고,
     * --   4. 사용자가 임의의 페이지에 접속할 때 실행될 명령을 작성해보자.
     *
     * -- String - INCR(++), DECR(--)
     * -- INCR articles:{id}
     * INCR articles:1
     * INCR articles:2
     */
    @GetMapping("/articles/{id}")
    @ResponseBody
    public String getViews(@PathVariable("id") Long id){

        redisTemplate.opsForValue().increment("articles:" +id);

        return String.valueOf(redisTemplate.opsForValue().get("articles:" +id));
    }


    /**
     * -- 2. 블로그에 로그인한 사람들의 조회수와 가장 많은 조회수를 기록한 글을 Redis로 확인하고싶다.
     * --   1. 블로그 url의 path는 '/articles/{id}' 형식이다.
     * --   2. 로그인 한 사람들의 계정은 영문으로만 이뤄져 있다.
     * --   3. 이를 관리하기 위해 적당한 데이터 타입을 선정하고,
     * --   4. 사용자가 입의의 페이지에 접속할 때 실행될 명령을 작성해보자.
     * --   4. 만약 상황에 따라 다른 명령이 실행되어야 한다면, 주석으로 추가해보자.
     *
     * -- Set
     * sadd articles:1 alex
     * sadd articles:1 brad
     * sadd articles:1 chad
     * scard articles:1
     *
     * sadd articles:2 alex
     * sadd articles:2 chad
     * scard  articles:2
     *
     * sadd articles:1 alex
     *
     * -- sadd의 결과에 따라 명령어를 실행하거나 말거나
     * -- 0은 스킵
     * -- 1인 경우? sorted set
     * zadd articles:ranks 1 articles:1
     * zincrby  articles:ranks 1 articles:1
     *
     * -- 가장 높은 조회수를 보고싶다면
     * zrevrange  articles:ranks 0 0
     * zrange articles:ranks 0 0 REV
     */

    @GetMapping("/articles/rank/{id}/{username}")
    public String getRank(@PathVariable("id") Long id, @PathVariable("username")String username){
        String userKey = "articles:" + id;
        String rankKey = "articles:ranks";

        // 사용자 조회 여부 확인 (sadd : 새로운 유저가 조회하면 1 , 아니면 0 반환)
        Long added = redisTemplate.opsForSet().add(userKey,username);

        // 새로운 사용자라면 조회수 증가
        if (added != null && added == 1){
            redisTemplate.opsForZSet().incrementScore(rankKey,userKey,1);
        }

        Long views = redisTemplate.opsForSet().size(userKey);

        // 특정 게시글을 조회수 확인
        return  "Article " + id + " 조회수 : " + views;
    }

    @GetMapping("/articles/top")
    public String getTopArticle() {
        String rankKey = "articles:ranks";

        // 조회수가 가장 높은 게시글 가져오기 (최상위 1개)
        Set<ZSetOperations.TypedTuple<Object>> topArticle =
                redisTemplate.opsForZSet().reverseRangeWithScores(rankKey, 0, 0);

        if (topArticle != null && !topArticle.isEmpty()) {
            ZSetOperations.TypedTuple<Object> article = topArticle.iterator().next();
            return "가장 많이 본 게시글: " + article.getValue() + " (조회수: " + article.getScore() + ")";
        }
        return "조회된 게시글이 없습니다.";
    }

}
