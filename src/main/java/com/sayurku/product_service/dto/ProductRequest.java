package com.sayurku.product_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductRequest(

        @NotNull(message = "Kategori wajib dipilih")
        UUID categoryId,

        @NotNull(message = "Vendor wajib diisi")
        UUID vendorId,

        @NotBlank(message = "Nama produk tidak boleh kosong")
        @Size(max = 200, message = "Nama produk maksimal 200 karakter")
        String name,

        String description,

        @NotNull(message = "Harga wajib diisi")
        @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih dari 0")
        BigDecimal price,

        @NotBlank(message = "Satuan wajib diisi (kg / ikat / buah)")
        String unit,

        @NotNull(message = "Stok wajib diisi")
        @Min(value = 0, message = "Stok tidak boleh minus")
        Integer stock,

        @NotNull(message = "Tanggal panen wajib diisi")
        @PastOrPresent(message = "Tanggal panen tidak boleh di masa depan")
        LocalDate harvestDate,

        @NotNull(message = "Masa kesegaran wajib diisi")
        @Min(value = 1, message = "Masa kesegaran minimal 1 hari")
        Integer freshnessDays,

        String imageUrl
) {}