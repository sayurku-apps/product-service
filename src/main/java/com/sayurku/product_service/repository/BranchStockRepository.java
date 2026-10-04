package com.sayurku.product_service.repository;

import com.sayurku.product_service.entity.BranchStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Nama method dibaca Spring jadi query, mis. findByBranchId...
// = WHERE branch_id = ? AND product.is_active = true AND stock > 0
public interface BranchStockRepository extends JpaRepository<BranchStock, UUID> {

    Optional<BranchStock> findByProductIdAndBranchId(UUID productId, UUID branchId);

    // Katalog satu cabang: hanya produk aktif yang stoknya masih ada
    @EntityGraph(attributePaths = {"branch", "product", "product.category"})
    Page<BranchStock> findByBranchIdAndProductIsActiveTrueAndStockGreaterThan(UUID branchId, int stock, Pageable pageable);

    // Stok satu produk di semua cabang aktif
    @EntityGraph(attributePaths = {"branch", "product", "product.category"})
    List<BranchStock> findByProductIdAndBranchIsActiveTrueOrderByBranchNameAsc(UUID productId);
}
