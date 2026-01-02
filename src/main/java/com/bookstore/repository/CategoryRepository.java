package com.bookstore.repository;

import com.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find a category by exact name
    Optional<Category> findByName(String name);

    // Find categories where name contains a keyword (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
