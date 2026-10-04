package com.sayurku.product_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(

        @NotNull(message = "Kategori wajib dipilih")
        UUID categoryId,

        @NotBlank(message = "Nama produk tidak boleh kosong")
        @Size(max = 200, message = "Nama produk maksimal 200 karakter")
        String name,

        String description,

        @NotNull(message = "Harga wajib diisi")
        @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih dari 0")
        BigDecimal price,

        @NotNull(message = "Harga modal wajib diisi")
        @DecimalMin(value = "0.0", inclusive = false, message = "Harga modal harus lebih dari 0")
        BigDecimal costPrice,

        @NotBlank(message = "Satuan wajib diisi (kg / ikat / buah)")
        String unit,

        @NotNull(message = "Masa kesegaran wajib diisi")
        @Min(value = 1, message = "Masa kesegaran minimal 1 hari")
        Integer freshnessDays,

        String imageUrl
) {}
