package com.app.restaurante.dto.request;

import com.app.restaurante.model.Category;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Category is required")
    private Category category;
}
    // @Size(min=3, max=50, message = "Name must be between 3 and 50 characters") longittud de caracteres permitidos
    // @Email(message = "Must be a valid email format")
    // @PositiveOrZero
    // @PastOrPresent
    // @Future

