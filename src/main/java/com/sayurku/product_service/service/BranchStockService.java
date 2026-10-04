package com.sayurku.product_service.service;

import com.sayurku.product_service.dto.BranchStockRequest;
import com.sayurku.product_service.dto.BranchStockResponse;
import com.sayurku.product_service.entity.Branch;
import com.sayurku.product_service.entity.BranchStock;
import com.sayurku.product_service.entity.Product;
import com.sayurku.product_service.repository.BranchStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchStockService {

    private final BranchStockRepository branchStockRepository;
    private final BranchService branchService;
    private final ProductService productService;

    /** Katalog satu cabang: produk yang stoknya masih ada di sana */
    @Transactional(readOnly = true)
    public Page<BranchStockResponse> findByBranch(UUID branchId, Pageable pageable) {
        branchService.getEntityById(branchId);   // 404 kalau cabangnya nggak ada
        return branchStockRepository
                .findByBranchIdAndProductIsActiveTrueAndStockGreaterThan(branchId, 0, pageable)
                .map(BranchStockResponse::from);
    }

    /** Stok satu produk di semua cabang */
    @Transactional(readOnly = true)
    public List<BranchStockResponse> findByProduct(UUID productId) {
        productService.getEntityById(productId);
        return branchStockRepository.findByProductIdAndBranchIsActiveTrueOrderByBranchNameAsc(productId).stream()
                .map(BranchStockResponse::from)
                .toList();
    }

    /** Set stok produk di cabang. Belum ada barisnya -> dibuat (mirip updateOrCreate di Eloquent) */
    @Transactional
    public BranchStockResponse upsert(UUID branchId, UUID productId, BranchStockRequest request) {
        Branch branch = branchService.getEntityById(branchId);
        Product product = productService.getEntityById(productId);

        BranchStock stock = branchStockRepository.findByProductIdAndBranchId(productId, branchId)
                .orElseGet(() -> BranchStock.builder()
                        .branch(branch)
                        .product(product)
                        .build());

        stock.setStock(request.stock());
        stock.setHarvestDate(request.harvestDate());

        return BranchStockResponse.from(branchStockRepository.save(stock));
    }
}
