package com.app.dto;

import java.util.List;

public class OrderDetailDTO {

    private Long id;
    private String customerEmail;
    private String status;
    private List<OrderItemOutputDTO> items;
    private Integer totalCents;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<OrderItemOutputDTO> getItems() {
        return items;
    }
    public void setItems(List<OrderItemOutputDTO> items) {
        this.items = items;
    }
    public Integer getTotalCents() {
        return totalCents;
    }
    public void setTotalCents(Integer totalCents) {
        this.totalCents = totalCents;
    }
    
}
