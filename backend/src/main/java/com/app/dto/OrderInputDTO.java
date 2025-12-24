package com.app.dto;

import java.util.List;

public class OrderInputDTO {
   
    private String customerEmail;
    private List<OrderItemInputDTO> items;
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public List<OrderItemInputDTO> getItems() {
        return items;
    }
    public void setItems(List<OrderItemInputDTO> items) {
        this.items = items;
    }

}
