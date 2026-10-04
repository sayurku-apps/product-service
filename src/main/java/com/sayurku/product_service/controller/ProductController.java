package com.sayurku.product_service.controller;

import com.sayurku.product_service.dto.ProductRequest;
import com.sayurku.product_service.dto.ProductResponse;
import com.sayurku.product_service.dto.StockUpdateRequest;
import com.sayurku.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ?categoryId= &page= &size= &sort=name,asc
    @GetMapping
    public PagedModel<ProductResponse> findAll(@RequestParam(required = false) UUID categoryId,
                                               Pageable pageable) {
        return new PagedModel<>(productService.findAll(categoryId, pageable));
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse updateStock(@PathVariable UUID id, @Valid @RequestBody StockUpdateRequest request) {
        return productService.updateStock(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.softDelete(id);
    }
}
