package com.app.dto;

public class PaymentInputDTO {
    
    private Long orderId;
    private Integer amountCents;

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Integer getAmountCents() {
        return amountCents;
    }
    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }


}
