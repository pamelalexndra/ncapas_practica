package com.app.restaurante.services;

import com.app.restaurante.dto.request.ProductRequest;
import com.app.restaurante.dto.response.ProductResponse;
import com.app.restaurante.exception.DuplicateEntityException;
import com.app.restaurante.exception.EntityNotFoundException;
import com.app.restaurante.model.Category;
import com.app.restaurante.model.Product;
import com.app.restaurante.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import com.app.restaurante.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAll(Category category, Boolean available) {
        if (category != null && available != null) {
            return productRepository.findByCategoryAndAvailable(category, available).stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateEntityException("Product", "name", request.getName());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());

        // Regla de Negocio implícita
        product.setAvailable(product.getQuantity() > 0);
        product.setCreatedAt(LocalDateTime.now());

        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));

        if (productRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new DuplicateEntityException("Product", "name", request.getName());
        }

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setQuantity(request.getQuantity());
        existing.setCategory(request.getCategory());

        if (existing.getQuantity() == 0) {
            existing.setAvailable(false);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return toResponse(productRepository.save(existing));
    }

    public ProductResponse updateStockPartial(Long id, Integer amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));

        int targetStock = product.getQuantity() + amount;
        if (targetStock < 0) {
            throw new BusinessRuleException("Resulting stock cannot be less than 0");
        }

        product.setQuantity(targetStock);
        if (product.getQuantity() == 0) {
            product.setAvailable(false);
        }

        product.setUpdatedAt(LocalDateTime.now());
        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateAvailabilityManual(Long id, Boolean available) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));

        if (product.getQuantity() == 0 && available) {
            throw new BusinessRuleException("Cannot set availability to true if stock is 0");
        }

        product.setAvailable(available);
        product.setUpdatedAt(LocalDateTime.now());
        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));

        if (product.getCategory() == Category.INGREDIENT && product.getAvailable()) {
            throw new BusinessRuleException("Cannot delete available ingredients");
        }
        productRepository.delete(product);
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setAvailable(product.getAvailable());
        response.setCategory(product.getCategory());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}