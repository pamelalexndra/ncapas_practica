package com.app.restaurante.dto.request;

import jakarta.validation.constraints.NotNull;

public class StockUpdateRequest {

    @NotNull(message = "Amount is required")
    private Integer amount;

    public StockUpdateRequest() {}

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}