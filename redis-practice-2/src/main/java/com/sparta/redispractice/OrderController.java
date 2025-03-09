package com.sparta.redispractice;

import com.sparta.redispractice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    //create
    @PostMapping("/orders")
    public ItemOrder addOrder(@RequestBody ItemOrder itemOrder){
        return orderRepository.save(itemOrder);
    }

    //read - single
    @GetMapping("/orders/{id}")
    public ItemOrder getOrders(@PathVariable("id") String id){
        return orderRepository.findById(id).orElseThrow();
    }

    //read - list
    @GetMapping("/orders")
    public List<ItemOrder> getOrderList(){
        List<ItemOrder> list = new ArrayList<>();

        orderRepository.findAll().forEach(list :: add);

        return list;
    }

    // update
    @PatchMapping("/orders/{id}")
    public ItemOrder updateOrder(@PathVariable("id") String id,
                                 @RequestBody ItemOrder newItemOrder){
        ItemOrder itemOrder = orderRepository.findById(id).orElseThrow();

        ItemOrder updateOrder = itemOrder.toBuilder()
                .item(newItemOrder.getItem())
                .count(newItemOrder.getCount())
                .totalPrice(newItemOrder.getTotalPrice())
                .status(newItemOrder.getStatus())
                .build();

        return orderRepository.save(updateOrder);
    }


    // delete
    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable("id") String id){
        orderRepository.deleteById(id);
    }


}