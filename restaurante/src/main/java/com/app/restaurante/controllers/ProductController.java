package com.app.restaurante.controllers;

import com.app.restaurante.dto.request.ProductRequest;
import com.app.restaurante.dto.request.StockUpdateRequest;
import com.app.restaurante.dto.response.ProductResponse;
import com.app.restaurante.model.Category;
import com.app.restaurante.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Soporta GET /products y GET /products?category=FOOD&available=true
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Boolean available) {
        return ResponseEntity.ok(productService.getAll(category, available));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // PATCH para la actualización parcial de stock (amount positivo/negativo)
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStockPartial(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(productService.updateStockPartial(id, request.getAmount()));
    }

    // PATCH para alternar disponibilidad manualmente bajo reglas de negocio
    @PatchMapping("/{id}/availability")
    public ResponseEntity<ProductResponse> updateAvailabilityManual(
            @PathVariable Long id,
            @RequestParam Boolean available) {
        return ResponseEntity.ok(productService.updateAvailabilityManual(id, available));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Mapeos de GraphQL especificados en el formato de referencia del docente
    @QueryMapping
    public List<ProductResponse> allProducts() {
        return productService.getAll(null, null);
    }

    @QueryMapping
    public ProductResponse productById(@Argument Long id) {
        return productService.getById(id);
    }
}