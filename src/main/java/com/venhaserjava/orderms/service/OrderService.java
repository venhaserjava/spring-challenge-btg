package com.venhaserjava.orderms.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.venhaserjava.orderms.controller.dto.OrderResponse;
import com.venhaserjava.orderms.entity.OrderEntity;
import com.venhaserjava.orderms.entity.OrderItem;
import com.venhaserjava.orderms.listener.dto.OrderCreatedEvent;
import com.venhaserjava.orderms.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository,MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event){
        orderRepository.save(toEntity(event));
    }

    private OrderEntity toEntity(OrderCreatedEvent ev) {

        var entity = new OrderEntity();
        entity.setOrderId(ev.orderCode());
        entity.setCustomerId(ev.customerCode());
        entity.setItens(getOrderItems(ev));
        entity.setTotal(getTotal(ev));

        return entity;
    }

    private static BigDecimal getTotal(OrderCreatedEvent ev) {

        return ev.itens().stream()
            .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);               
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent ev){
        return ev.itens().stream()
        .map(i -> new OrderItem(i.product(), i.quantity(), i.price())).toList();
        
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);

    }
 
    @SuppressWarnings("null")
    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

}
