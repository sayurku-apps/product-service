package com.sayurku.product_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

// Dipakai order-service untuk mengurangi (reserve) atau mengembalikan (release) stok cabang
public record StockMovementRequest(
        @NotEmpty(message = "Item wajib diisi")
        List<@Valid Item> items
) {
    public record Item(
            @NotNull(message = "productId wajib diisi")
            UUID productId,

            @NotNull(message = "Jumlah wajib diisi")
            @Min(value = 1, message = "Jumlah minimal 1")
            Integer quantity
    ) {}
}
