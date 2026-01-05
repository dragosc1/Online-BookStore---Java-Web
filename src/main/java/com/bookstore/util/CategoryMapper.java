package com.bookstore.util;

import com.bookstore.dto.request.CategoryRequestDto;
import com.bookstore.dto.response.CategoryResponseDto;
import com.bookstore.model.Category;

public class CategoryMapper {

    public static CategoryResponseDto toResponseDto(Category category) {
        if (category == null) return null;
        return new CategoryResponseDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toEntity(CategoryRequestDto dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public static void updateEntity(Category category, CategoryRequestDto dto) {
        if (category == null || dto == null) return;
        category.setName(dto.getName());
    }
}
