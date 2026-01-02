package com.bookstore.controller;

import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Author;
import com.bookstore.model.Category;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookController(BookService bookService,
                          AuthorService authorService,
                          CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // ========= PUBLIC ENDPOINTS =========

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(
                bookService.getAllBooks()
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(BookMapper.toResponseDto(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(
                bookService.searchBooksByTitle(title)
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        Optional<Author> authorOpt = authorService.getAuthorById(authorId);
        if (authorOpt.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                bookService.getBooksByAuthor(authorOpt.get())
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByCategory(@PathVariable Long categoryId) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(categoryId);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                bookService.getBooksByCategory(categoryOpt.get())
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/instock")
    public ResponseEntity<List<BookResponseDto>> getBooksInStock() {
        return ResponseEntity.ok(
                bookService.getBooksInStock()
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/price")
    public ResponseEntity<List<BookResponseDto>> getBooksByPrice(@RequestParam Double max) {
        return ResponseEntity.ok(
                bookService.getBooksByPrice(max)
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
