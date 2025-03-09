package com.sparta.rediscachepractice3;

import com.sparta.rediscachepractice3.domain.StoreDto;
import com.sparta.rediscachepractice3.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final ServerProperties serverProperties;

    @PostMapping
    public StoreDto createStore(@RequestBody StoreDto storeDto){
        return storeService.createStore(storeDto);
    }

    @GetMapping("/{id}")
    public StoreDto readStore(@PathVariable("id")Long id){
        return storeService.readStore(id);
    }

    @GetMapping
    public List<StoreDto> readAll(){
        return storeService.readAll();
    }

    @PutMapping("/{id}")
    public StoreDto updateStore(@PathVariable("id")Long id, @RequestBody StoreDto storeDto){
        return storeService.updateStore(id,storeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable("id") Long id){
        storeService.deleteStore(id);
    }

    @GetMapping("/search")
    public Page<StoreDto> search(@RequestParam(name = "q")String query, Pageable pageable){
        return storeService.search(query,pageable);
    }
}
