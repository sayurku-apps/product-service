package com.sayurku.product_service.dto;

import com.sayurku.product_service.entity.Category;

import java.util.UUID;
public record CategoryResponse(
    UUID id,
    String name,
    String iconUrl
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(
            c.getId(),
            c.getName(),
            c.getIconUrl()
        );
    }
}
