package com.sparta.redispractice.repository;

import com.sparta.redispractice.ItemOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<ItemOrder,String> {
}
