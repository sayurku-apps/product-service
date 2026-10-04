package com.sayurku.product_service.dto;

import com.sayurku.product_service.entity.BranchStock;

import java.time.LocalDate;
import java.util.UUID;

// Stok satu produk di satu cabang, lengkap dengan info kesegarannya
public record BranchStockResponse(
        UUID branchId,
        String branchName,
        ProductResponse product,
        Integer stock,
        LocalDate harvestDate,
        LocalDate expiryDate,
        long remainingFreshnessDays,
        boolean stillFresh
) {
    public static BranchStockResponse from(BranchStock s) {
        return new BranchStockResponse(
                s.getBranch().getId(),
                s.getBranch().getName(),
                ProductResponse.from(s.getProduct()),
                s.getStock(),
                s.getHarvestDate(),
                s.expiryDate(),
                s.remainingFreshnessDays(),
                s.isStillFresh()
        );
    }
}
