package com.venhaserjava.orderms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.venhaserjava.orderms.entity.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

}
