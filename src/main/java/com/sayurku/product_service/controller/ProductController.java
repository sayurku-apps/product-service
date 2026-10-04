package com.sayurku.product_service.controller;

import com.sayurku.product_service.dto.BranchStockResponse;
import com.sayurku.product_service.dto.ProductRequest;
import com.sayurku.product_service.dto.ProductResponse;
import com.sayurku.product_service.service.BranchStockService;
import com.sayurku.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BranchStockService branchStockService;

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

    // Stok produk ini di tiap cabang
    @GetMapping("/{id}/stocks")
    public List<BranchStockResponse> findStocks(@PathVariable UUID id) {
        return branchStockService.findByProduct(id);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.softDelete(id);
    }
}
