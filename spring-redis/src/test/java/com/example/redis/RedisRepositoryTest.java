package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void createTest(){

        // 객체를 만들고
        Item item = Item.builder()
                .name("keyboard")
                .description("Very good")
                .price(100000)
                .build();

        // save 호출
        itemRepository.save(item);
    }

    @Test
    public void readOneTest(){
        Item item = itemRepository.findById("1L")
                .orElseThrow();
        System.out.println("item.getDescription() = " + item.getDescription());
    }

    @Test
    public void updateTest(){
        Item item = itemRepository.findById("1L")
                .orElseThrow();

        item.setDescription("On sale!");
        itemRepository.save(item);
        System.out.println("item.getDescription() = " + item.getDescription());
    }

    @Test
    public void deleteTest(){
        itemRepository.deleteById("1L");
    }

}
