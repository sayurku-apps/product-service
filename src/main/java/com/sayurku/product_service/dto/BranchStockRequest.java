package com.sayurku.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record BranchStockRequest(

        @NotNull(message = "Stok wajib diisi")
        @Min(value = 0, message = "Stok tidak boleh minus")
        Integer stock,

        @NotNull(message = "Tanggal panen wajib diisi")
        @PastOrPresent(message = "Tanggal panen tidak boleh di masa depan")
        LocalDate harvestDate
) {}
