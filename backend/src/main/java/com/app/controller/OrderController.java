package com.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.OrderDetailDTO;
import com.app.dto.OrderInputDTO;
import com.app.dto.OrderSummaryDTO;
import com.app.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderSummaryDTO> listOrders() {
        return service.listOrders();
    }

    @GetMapping("/{id}")
    public OrderDetailDTO getOrder(@PathVariable Long id) {
        return service.getOrderDetail(id);
    }

    @PostMapping
    public Long createOrder(@RequestBody OrderInputDTO input) {
        return service.createOrder(input);
    }
}