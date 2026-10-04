package com.sayurku.product_service.service;

import com.sayurku.product_service.dto.ProductRequest;
import com.sayurku.product_service.dto.ProductResponse;
import com.sayurku.product_service.entity.Category;
import com.sayurku.product_service.entity.Product;
import com.sayurku.product_service.exception.ResourceNotFoundException;
import com.sayurku.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(UUID categoryId, Pageable pageable) {
        Page<Product> products = (categoryId == null)
                ? productRepository.findByIsActiveTrue(pageable)
                : productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);

        return products.map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        return ProductResponse.from(getEntityById(id));
    }

    // create & update dipanggil admin (wajib login), jadi respons menyertakan harga modal

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryService.getEntityById(request.categoryId());

        Product product = Product.builder()
                .category(category)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .costPrice(request.costPrice())
                .unit(request.unit())
                .freshnessDays(request.freshnessDays())
                .imageUrl(request.imageUrl())
                .isActive(true)
                .build();

        return ProductResponse.withCost(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = getEntityById(id);
        Category category = categoryService.getEntityById(request.categoryId());

        product.setCategory(category);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCostPrice(request.costPrice());
        product.setUnit(request.unit());
        product.setFreshnessDays(request.freshnessDays());
        product.setImageUrl(request.imageUrl());

        return ProductResponse.withCost(product);
    }

    @Transactional
    public void softDelete(UUID id) {
        Product product = getEntityById(id);
        product.setIsActive(false);
    }

    @Transactional(readOnly = true)
    public Product getEntityById(UUID id) {
        // produk yang sudah di-soft-delete dianggap tidak ada
        return productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan: " + id));
    }
}
