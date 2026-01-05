package com.bookstore.controller.admin;

import com.bookstore.dto.request.CategoryRequestDto;
import com.bookstore.dto.response.CategoryResponseDto;
import com.bookstore.model.Category;
import com.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AdminCategoryController controller;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
    }

    // ========= CREATE =========
    @Test
    void createCategory_success() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Fiction");

        when(categoryService.saveCategory(any(Category.class)))
                .thenReturn(category);

        ResponseEntity<CategoryResponseDto> response =
                controller.createCategory(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Fiction", response.getBody().getName());

        verify(categoryService).saveCategory(any(Category.class));
    }

    // ========= READ =========
    @Test
    void getCategoryById_found() {
        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.of(category));

        ResponseEntity<CategoryResponseDto> response =
                controller.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Fiction", response.getBody().getName());
    }

    @Test
    void getCategoryById_notFound() {
        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<CategoryResponseDto> response =
                controller.getCategoryById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllCategories_success() {
        when(categoryService.getAllCategories())
                .thenReturn(List.of(category));

        ResponseEntity<List<CategoryResponseDto>> response =
                controller.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Fiction", response.getBody().get(0).getName());
    }

    // ========= UPDATE =========
    @Test
    void updateCategory_success() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Updated Fiction");

        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.of(category));
        when(categoryService.saveCategory(any(Category.class)))
                .thenReturn(category);

        ResponseEntity<CategoryResponseDto> response =
                controller.updateCategory(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Fiction", response.getBody().getName());

        verify(categoryService).saveCategory(category);
    }

    @Test
    void updateCategory_notFound() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Doesn't Matter");

        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<CategoryResponseDto> response =
                controller.updateCategory(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(categoryService, never()).saveCategory(any());
    }

    // ========= DELETE =========
    @Test
    void deleteCategory_success() {
        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.of(category));

        ResponseEntity<Void> response =
                controller.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(categoryService).deleteCategoryById(1L);
    }

    @Test
    void deleteCategory_notFound() {
        when(categoryService.getCategoryById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<Void> response =
                controller.deleteCategory(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(categoryService, never()).deleteCategoryById(any());
    }
}
