package com.bookstore.service.impl;

import com.bookstore.model.Category;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> searchCategoriesByName(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
