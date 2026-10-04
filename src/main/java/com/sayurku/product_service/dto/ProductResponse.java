package com.sayurku.product_service.dto;

import com.sayurku.product_service.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String unit,
        Integer stock,
        LocalDate harvestDate,
        Integer freshnessDays,
        long remainingFreshnessDays,
        boolean stillFresh,
        String imageUrl,
        UUID vendorId,
        CategoryResponse category
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getUnit(),
                p.getStock(),
                p.getHarvestDate(),
                p.getFreshnessDays(),
                p.remainingFreshnessDays(),
                p.isStillFresh(),
                p.getImageUrl(),
                p.getVendorId(),
                CategoryResponse.from(p.getCategory())
        );
    }
}