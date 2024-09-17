package com.venhaserjava.orderms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.venhaserjava.orderms.entity.OrderEntity;
import com.venhaserjava.orderms.entity.OrderItem;
import com.venhaserjava.orderms.listener.dto.OrderCreatedEvent;
import com.venhaserjava.orderms.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

}
