package com.sparta.rediscachepractice3;

import com.sparta.rediscachepractice3.domain.Store;
import com.sparta.rediscachepractice3.domain.StoreDto;
import com.sparta.rediscachepractice3.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 새로 만든 상점은 다음 검색에서 등장할 수 있게끔 기존의 전체 조회 캐시 삭제
    // 인지도가 높지않을 가능성이 있으므로 생성시 추가 캐시 생성은 하지 않는다.
    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    public StoreDto createStore(StoreDto storeDto) {
        return StoreDto.formEntity(storeRepository.save(Store.builder()
                .name(storeDto.getName())
                .category(storeDto.getCategory())
                .build()));
    }

    // 조회된 상점은 일단 캐시에 저장
    // ttl은 줄이면서 tti를 설정함으로서,
    // 자주 조회되지 않는 삼정들은 캐시에서 빠르게 제거하고
    // 자줒 조회되는 상점은 캐시에 유지되도록 설정
    @Cacheable(cacheNames = "storeCache", key = "#id")
    public StoreDto readStore(Long id) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        return StoreDto.formEntity(store);
    }

    // 전체 조회는 이반적인 서비스에서 가장 많이 활용
    @Cacheable(cacheNames = "storeAllCache", key = "getMethodName()")
    public List<StoreDto> readAll() {
        return storeRepository.findAll().stream()
                .map(StoreDto:: formEntity)
                .toList();
    }

    // 상점이 갱신될 경우 해당 내용을 갱신
    // 단일 캐시는 갱신, 전체 캐시는 제거한다.
    @CachePut(cacheNames = "storeCache", key = "#id")
    @Caching(evict = {
            @CacheEvict(cacheNames = "storeCache", key = "#id"),// 개별 아이템 캐시 제거
            @CacheEvict(cacheNames = "storeAllCache", key = "'readAll'") // 전체 목록 캐시 제거
    })
    public StoreDto updateStore(Long id, StoreDto storeDto) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        Store updateStore = store.toBuilder()
                .name(storeDto.getName())
                .category(storeDto.getCategory())
                .build();

        return StoreDto.formEntity(storeRepository.save(updateStore));

    }

    //삭제될 경우 단일캐시 ,전체 캐시 전부 초기화
    @Caching(evict = {
            @CacheEvict(cacheNames = "storeCache", key = "#id"),// 개별 아이템 캐시 제거
            @CacheEvict(cacheNames = "storeAllCache", allEntries = true) // 전체 목록 캐시 제거
    })
    public void deleteStore(Long id) {
      storeRepository.deleteById(id);
    }


    @Cacheable(cacheNames = "itemSearchCache",
            key = "{ args[0], args[1].pageNumber, args[1].pageSize }")
    public Page<StoreDto> search(String query, Pageable pageable) {
        return  storeRepository.findAllByNameContains(query,pageable)
                .map(StoreDto :: formEntity);
    }
}
