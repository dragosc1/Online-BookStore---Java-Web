package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.Author;
import com.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find a book by exact ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find books by title containing keyword (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by author
    List<Book> findByAuthors(Author author);

    // Find books by category
    List<Book> findByCategories(Category category);

    // Find books with price less than or equal to a given value
    List<Book> findByPriceLessThanEqual(Double price);

    // Find books with stock greater than 0 (available for purchase)
    List<Book> findByStockGreaterThan(Integer stock);
}