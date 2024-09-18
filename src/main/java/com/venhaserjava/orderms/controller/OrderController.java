package com.venhaserjava.orderms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.venhaserjava.orderms.controller.dto.ApiResponse;
import com.venhaserjava.orderms.controller.dto.OrderResponse;
import com.venhaserjava.orderms.service.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        return ResponseEntity.ok(null);                                           
    }
}
