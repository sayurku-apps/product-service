package com.sayurku.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(

        @NotNull(message = "Stok wajib diisi")
        @Min(value = 0, message = "Stok tidak boleh minus")
        Integer stock
) {}