package com.sayurku.product_service.dto;

import com.sayurku.product_service.entity.Branch;

import java.util.UUID;

public record BranchResponse(
        UUID id,
        String name,
        String address,
        String phone
) {
    public static BranchResponse from(Branch b) {
        return new BranchResponse(b.getId(), b.getName(), b.getAddress(), b.getPhone());
    }
}
