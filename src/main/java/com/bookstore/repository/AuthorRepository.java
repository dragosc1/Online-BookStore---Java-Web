package com.bookstore.repository;

import com.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Find an author by name
    Optional<Author> findByName(String name);

    // Find all authors whose names contain a keyword (useful for search)
    List<Author> findByNameContainingIgnoreCase(String keyword);
}