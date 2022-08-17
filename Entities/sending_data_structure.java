package com.example.bigfamilyv20.Entities;

public class sending_data_structure {
    int finalAmount;
    Long productId;

    public sending_data_structure(int finalAmount, Long productId) {
        this.finalAmount = finalAmount;
        this.productId = productId;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
