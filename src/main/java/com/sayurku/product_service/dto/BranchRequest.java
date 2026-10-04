package com.sayurku.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequest(

        @NotBlank(message = "Nama cabang tidak boleh kosong")
        @Size(max = 100, message = "Nama cabang maksimal 100 karakter")
        String name,

        @NotBlank(message = "Alamat cabang wajib diisi")
        String address,

        @Size(max = 20, message = "Nomor telepon maksimal 20 karakter")
        String phone
) {}
