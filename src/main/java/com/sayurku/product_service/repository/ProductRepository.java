package com.sayurku.product_service.repository;

import com.sayurku.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

// @EntityGraph("category") = ambil kategori sekalian lewat JOIN,
// biar nggak N+1 (1 query produk + 1 query per kategori).
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = "category")
    Optional<Product> findByIdAndIsActiveTrue(UUID id);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByIsActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByIsActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}
