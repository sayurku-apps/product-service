package com.sayurku.product_service.dto;

import com.sayurku.product_service.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

// Khusus jalur /internal (antar-service). Berisi harga modal, jadi JANGAN dipakai di /api.
public record InternalProductResponse(
        UUID id,
        String name,
        String unit,
        BigDecimal price,
        BigDecimal costPrice,
        boolean active
) {
    public static InternalProductResponse from(Product p) {
        return new InternalProductResponse(p.getId(), p.getName(), p.getUnit(),
                p.getPrice(), p.getCostPrice(), p.getIsActive());
    }
}
