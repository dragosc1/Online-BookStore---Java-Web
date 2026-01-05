package com.bookstore.service;

import com.bookstore.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category saveCategory(Category category);

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    Optional<Category> getCategoryByName(String name);

    List<Category> searchCategoriesByName(String keyword);

    void deleteCategoryById(Long id);
}
