package com.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.dto.OrderDetailDTO;
import com.app.dto.OrderInputDTO;
import com.app.dto.OrderItemOutputDTO;
import com.app.dto.OrderSummaryDTO;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.Payment;
import com.app.model.Product;
import com.app.repository.OrderRepository;
import com.app.repository.PaymentRepository;
import com.app.repository.ProductRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository,
            ProductRepository productRepository,
            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
    }

    // 🔹 Criar pedido
    public Long createOrder(OrderInputDTO input) {

        Order order = new Order();
        order.setCustomerEmail(input.getCustomerEmail());
        order.setStatus("OPEN");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = input.getItems().stream().map(itemDTO -> {

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (!product.getActive()) {
                throw new RuntimeException("Produto inativo não pode ser adicionado ao pedido");
            }

            if (itemDTO.getQuantity() <= 0) {
                throw new RuntimeException("Quantidade deve ser maior que zero");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPriceCents(product.getUnitPriceCents());

            return item;

        }).collect(Collectors.toList());

        order.setItems(items);

        Order saved = orderRepository.save(order);
        return saved.getId();
    }

    // 🔹 Listar pedidos resumidos
    public List<OrderSummaryDTO> listOrders() {
        return orderRepository.findAll().stream().map(order -> {

            OrderSummaryDTO dto = new OrderSummaryDTO();
            dto.setId(order.getId());
            dto.setCustomerEmail(order.getCustomerEmail());
            dto.setStatus(order.getStatus());
            dto.setTotalCents(calculateTotal(order));

            return dto;
        }).collect(Collectors.toList());
    }

    // 🔹 Detalhe do pedido
    public OrderDetailDTO getOrderDetail(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(order.getId());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setStatus(order.getStatus());

        List<OrderItemOutputDTO> items = order.getItems().stream().map(item -> {

            OrderItemOutputDTO itemDTO = new OrderItemOutputDTO();
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPriceCents(item.getUnitPriceCents());
            itemDTO.setSubtotalCents(item.getQuantity() * item.getUnitPriceCents());

            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);
        dto.setTotalCents(calculateTotal(order));

        return dto;
    }

    // 🔹 Regra central do PDF
    private Integer calculateTotal(Order order) {
        return order.getItems().stream()
                .mapToInt(i -> i.getQuantity() * i.getUnitPriceCents())
                .sum();
    }

    // 🔹 Atualiza status se pago
    public void updateStatusIfPaid(Order order) {
        int total = calculateTotal(order);

        int paid = paymentRepository.findByOrderId(order.getId())
                .stream()
                .mapToInt(Payment::getAmountCents)
                .sum();

        if (paid >= total) {
            order.setStatus("PAID");
            orderRepository.save(order);
        }
    }
}
