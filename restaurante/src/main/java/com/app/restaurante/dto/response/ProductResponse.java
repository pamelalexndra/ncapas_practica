package com.app.restaurante.dto.response;

import com.app.restaurante.model.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Boolean available;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}