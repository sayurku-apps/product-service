package com.sayurku.product_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sayurku.product_service.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        // null = tidak ikut dikirim di JSON. Pembeli nggak boleh tahu harga modal.
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal costPrice,
        String unit,
        Integer freshnessDays,
        String imageUrl,
        CategoryResponse category
) {
    /** Untuk publik: tanpa harga modal */
    public static ProductResponse from(Product p) {
        return build(p, null);
    }

    /** Untuk admin: dengan harga modal */
    public static ProductResponse withCost(Product p) {
        return build(p, p.getCostPrice());
    }

    private static ProductResponse build(Product p, BigDecimal costPrice) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                costPrice,
                p.getUnit(),
                p.getFreshnessDays(),
                p.getImageUrl(),
                CategoryResponse.from(p.getCategory())
        );
    }
}
