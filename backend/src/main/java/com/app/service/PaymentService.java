package com.app.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.app.dto.PaymentInputDTO;
import com.app.model.Payment;
import com.app.repository.OrderRepository;
import com.app.repository.PaymentRepository;

import com.app.model.Order;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public void registerPayment(PaymentInputDTO input) {

        Order order = orderRepository.findById(input.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountCents(input.getAmountCents());
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        orderService.updateStatusIfPaid(order);
    }
}