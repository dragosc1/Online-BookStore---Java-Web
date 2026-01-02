package com.bookstore.controller.admin;

import com.bookstore.dto.request.CategoryRequestDto;
import com.bookstore.dto.response.CategoryResponseDto;
import com.bookstore.util.CategoryMapper;
import com.bookstore.model.Category;
import com.bookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ========= CREATE =========
    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto dto) {
        Category category = CategoryMapper.toEntity(dto);
        Category saved = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryMapper.toResponseDto(saved));
    }

    // ========= READ =========
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt
                .map(category -> ResponseEntity.ok(CategoryMapper.toResponseDto(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories()
                .stream()
                .map(CategoryMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // ========= UPDATE =========
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDto dto) {

        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        Category category = categoryOpt.get();
        CategoryMapper.updateEntity(category, dto);
        Category updated = categoryService.saveCategory(category);

        return ResponseEntity.ok(CategoryMapper.toResponseDto(updated));
    }

    // ========= DELETE =========
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.getCategoryById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
