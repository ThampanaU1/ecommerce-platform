package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.CategoryRequest;
import com.ecommerce.backend.catalog.dto.CategoryResponse;
import com.ecommerce.backend.catalog.entity.Category;
import com.ecommerce.backend.catalog.repository.CategoryRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category name already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setActive(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setUpdatedAt(LocalDateTime.now());

        Category updated = categoryRepository.save(category);

        return toResponse(updated);
    }

    @Transactional
    public CategoryResponse setActiveStatus(Long id, boolean active) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        category.setActive(active);
        category.setUpdatedAt(LocalDateTime.now());

        Category updated = categoryRepository.save(category);

        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

    public CategoryResponse getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        return toResponse(category);
    }

    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getDisplayOrder(),
                category.getActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}