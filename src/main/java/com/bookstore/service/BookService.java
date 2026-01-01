package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.Author;
import com.bookstore.model.Category;

import java.util.List;
import java.util.Optional;

public interface BookService {

    // Create or update a book
    Book saveBook(Book book);

    // Get all books
    List<Book> getAllBooks();

    // Get book by id
    Optional<Book> getBookById(Long id);

    // Search books by title
    List<Book> searchBooksByTitle(String title);

    // Get books by author
    List<Book> getBooksByAuthor(Author author);

    // Get books by category
    List<Book> getBooksByCategory(Category category);

    // Find book by ISBN
    Optional<Book> getBookByIsbn(String isbn);

    // Delete a book by id
    void deleteBook(Long id);

    // Get books in stock
    List<Book> getBooksInStock();

    // Check stock
    void checkStock(Book book, int quantityRequested);

    // Get books under a price
    List<Book> getBooksByPrice(Double maxPrice);
}
