package com.sayurku.product_service.service;

import com.sayurku.product_service.dto.InternalProductResponse;
import com.sayurku.product_service.dto.StockMovementRequest;
import com.sayurku.product_service.entity.Product;
import com.sayurku.product_service.exception.InsufficientStockException;
import com.sayurku.product_service.repository.BranchStockRepository;
import com.sayurku.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Dipanggil order-service, bukan pembeli langsung.
@Service
@RequiredArgsConstructor
public class InternalStockService {

    private final ProductRepository productRepository;
    private final BranchStockRepository branchStockRepository;
    private final ProductService productService;
    private final BranchService branchService;

    /** Data beberapa produk sekaligus (termasuk yang nonaktif, ditandai active=false) */
    @Transactional(readOnly = true)
    public List<InternalProductResponse> findProducts(List<UUID> ids) {
        return productRepository.findAllById(ids).stream()
                .map(InternalProductResponse::from)
                .toList();
    }

    /**
     * Checkout: kurangi stok cabang untuk semua item, lalu kembalikan snapshot harganya.
     * Satu transaksi: kalau satu item gagal, pengurangan item sebelumnya ikut dibatalkan.
     */
    @Transactional
    public List<InternalProductResponse> reserve(UUID branchId, StockMovementRequest request) {
        branchService.getEntityById(branchId);   // 404 kalau cabang tidak ada / tutup

        List<InternalProductResponse> snapshots = new ArrayList<>();
        for (StockMovementRequest.Item item : request.items()) {
            Product product = productService.getEntityById(item.productId());   // 404 kalau tidak aktif

            // Masih segar = dipanen paling lama freshnessDays hari yang lalu (sama dengan BranchStock.isStillFresh)
            LocalDate minHarvestDate = LocalDate.now().minusDays(product.getFreshnessDays());
            int updated = branchStockRepository.decreaseStock(item.productId(), branchId, item.quantity(), minHarvestDate);
            if (updated == 0) {
                throw new InsufficientStockException(rejectReason(product, branchId, item.quantity()));
            }
            snapshots.add(InternalProductResponse.from(product));
        }
        return snapshots;
    }

    // Pengurangan stok ditolak: cari tahu kenapa, supaya pesannya jelas untuk pembeli
    private String rejectReason(Product product, UUID branchId, int quantity) {
        return branchStockRepository.findByProductIdAndBranchId(product.getId(), branchId)
                .filter(s -> s.getStock() >= quantity && !s.isStillFresh())
                .map(s -> "Stok " + product.getName() + " di cabang ini sudah tidak segar")
                .orElse("Stok " + product.getName() + " di cabang ini tidak cukup");
    }

    /** Pesanan batal: stok dikembalikan ke cabang */
    @Transactional
    public void release(UUID branchId, StockMovementRequest request) {
        for (StockMovementRequest.Item item : request.items()) {
            branchStockRepository.increaseStock(item.productId(), branchId, item.quantity());
        }
    }
}
