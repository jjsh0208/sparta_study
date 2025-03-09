package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.repo.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(itemRepository.save(Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build()));
    }

    // 이 메서드의 결과는 캐싱이 가능하다.
    // cacheNames : 적용할 캐시 규칙을 지정하기 위한 이름 (이 메서드로 인해서 만들어질 캐시를 지칭하는 이름)
    // key : 캐시 데이터를 구분하기 위해 활용하는 값
    @Cacheable(cacheNames = "itemCache", key = "args[0]")
    public ItemDto readOne(Long id) {
        log.info("Read One = {}", id);
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Cacheable(cacheNames = "itemAllCache", key = "getMethodName()")
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemDto::fromEntity)
                .toList();
    }



    @CachePut(cacheNames = "itemCache", key = "args[0]")
    // 변경이 생기면 기존의 읽어오는 캐시를 제거한다.
    @CacheEvict(cacheNames = "itemAllCache", key = "'readAll'")
    public ItemDto update(Long id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(itemRepository.save(item));
    }


    @Caching(evict = {
            @CacheEvict(cacheNames = "itemCache", key = "#id"),// 개별 아이템 캐시 제거
            @CacheEvict(cacheNames = "itemAllCache", key = "'readAll'") // 전체 목록 캐시 제거
    })
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Cacheable(cacheNames = "itemSearchCache",
            key = "{ args[0], args[1].pageNumber, args[1].pageSize }")
    public Page<ItemDto> searchByName(String query, Pageable pageable){
        return itemRepository.findAllByNameContains(query,pageable).map(ItemDto::fromEntity);
    }


}

// 레디스 캐시 콘픽의 여러개의 콕픽을 넣는 방법들
// .withInitialCacheConfigurations() 란 무엇인지