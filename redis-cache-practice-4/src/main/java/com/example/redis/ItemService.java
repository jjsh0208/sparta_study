package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.domain.ItemOrder;
import com.example.redis.domain.ItemOrderDto;
import com.example.redis.repository.ItemRepository;
import com.example.redis.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ZSetOperations<String, ItemDto> rankOps;
    private final RedisTemplate<String,ItemOrderDto> orderTemplate;
    private final ListOperations<String, ItemOrderDto> orderOps;

    public ItemService(
            ItemRepository itemRepository,
            OrderRepository orderRepository,
            RedisTemplate<String, ItemDto> rankTemplate, ListOperations<String, ItemOrderDto> orderOps, RedisTemplate<String, ItemOrderDto> ordertemplate
    ) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.rankOps  = rankTemplate.opsForZSet();
        this.orderOps = orderOps;
        this.orderTemplate = ordertemplate;
    }

    @Transactional
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    public void insertOrders(){
        boolean exists = Optional.ofNullable(orderTemplate.hasKey("orderCache::behind"))
                .orElse(false);

        if (!exists){
            log.info("no orders in cache");
            return;
        }

        // 적재된 주문을 처리하기 위해 별도로 이름을 변경하기 위해
        orderTemplate.rename("orderCache::behind","orderCache::now");
        
        // nullable 검사도 해야함
        orderRepository.saveAll(orderOps.range("orderCache::now",0,-1).stream()
                .map(dto -> ItemOrder.builder()
                        .itemId(dto.getItemId())
                        .count(dto.getCount())
                        .build())
                .toList());
        orderTemplate.delete("orderCache::now");
    }


    public void purchase(ItemOrderDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        orderOps.rightPush("orderCache::behind",dto);
        rankOps.incrementScore("soldRanks",ItemDto.fromEntity(item),1);

    }

    public List<ItemDto> getMostSold(){
        Set<ItemDto> ranks = rankOps.reverseRange("soldRanks",0,9);
        if (ranks == null){
            return Collections.emptyList();
        }

        return ranks.stream().toList();
    }

}
