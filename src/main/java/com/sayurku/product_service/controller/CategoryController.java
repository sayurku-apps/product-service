package com.sayurku.product_service.controller;

import com.sayurku.product_service.dto.CategoryRequest;
import com.sayurku.product_service.dto.CategoryResponse;
import com.sayurku.product_service.security.AccessPolicy;
import com.sayurku.product_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestHeader(AccessPolicy.ROLE) String role,
                                   @Valid @RequestBody CategoryRequest request) {
        AccessPolicy.requireAdmin(role);
        return categoryService.create(request);
    }
}
