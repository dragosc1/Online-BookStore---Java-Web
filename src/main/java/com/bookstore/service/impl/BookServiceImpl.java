package com.bookstore.service.impl;

import com.bookstore.exception.InsufficientStockException;
import com.bookstore.model.Book;
import com.bookstore.model.Author;
import com.bookstore.model.Category;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        return bookRepository.findByAuthors(author);
    }

    @Override
    public List<Book> getBooksByCategory(Category category) {
        return bookRepository.findByCategories(category);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksInStock() {
        return bookRepository.findByStockGreaterThan(0);
    }

    @Override
    public List<Book> getBooksByPrice(Double maxPrice) {
        return bookRepository.findByPriceLessThanEqual(maxPrice);
    }

    @Override
    public void checkStock(Book book, int quantityRequested) {
        if (book.getStock() < quantityRequested) {
            throw new InsufficientStockException(
                    "Insufficient stock for book '" + book.getTitle() + "'. Requested: "
                            + quantityRequested + ", Available: " + book.getStock()
            );
        }
    }
}
