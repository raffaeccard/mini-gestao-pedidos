package com.app.dto;

public class OrderItemOutputDTO {
    private String productName;
    private Integer quantity;
    private Integer unitPriceCents;
    private Integer subtotalCents;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getUnitPriceCents() {
        return unitPriceCents;
    }
    public void setUnitPriceCents(Integer unitPriceCents) {
        this.unitPriceCents = unitPriceCents;
    }
    public Integer getSubtotalCents() {
        return subtotalCents;
    }
    public void setSubtotalCents(Integer subtotalCents) {
        this.subtotalCents = subtotalCents;
    }

}


