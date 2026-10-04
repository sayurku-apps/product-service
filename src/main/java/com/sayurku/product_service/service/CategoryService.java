package com.sayurku.product_service.service;

import com.sayurku.product_service.dto.CategoryRequest;
import com.sayurku.product_service.dto.CategoryResponse;
import com.sayurku.product_service.entity.Category;
import com.sayurku.product_service.exception.DuplicateResourceException;
import com.sayurku.product_service.exception.ResourceNotFoundException;
import com.sayurku.product_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Kategori '" + request.name() + "' sudah ada");
        }

        Category category = Category.builder()
                .name(request.name())
                .iconUrl(request.iconUrl())
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public Category getEntityById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori tidak ditemukan: " + id));
    }
}