package com.bookstore.controller;

import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
@Tag(
        name = "Books (Public)",
        description = "Public endpoints to view and search books"
)
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

    // ========= GET ALL BOOKS =========
    @Operation(
            summary = "Get all books",
            description = "Retrieve a list of all books"
    )
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(
                bookService.getAllBooks()
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    // ========= GET BOOK BY ID =========
    @Operation(
            summary = "Get book by ID",
            description = "Retrieve details of a single book by its ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(BookMapper.toResponseDto(book));
    }

    // ========= SEARCH BOOKS BY TITLE =========
    @Operation(
            summary = "Search books by title",
            description = "Search for books containing the given title string"
    )
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(
                bookService.searchBooksByTitle(title)
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    // ========= GET BOOKS BY AUTHOR =========
    @Operation(
            summary = "Get books by author",
            description = "Retrieve all books written by a specific author"
    )
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

    // ========= GET BOOKS BY CATEGORY =========
    @Operation(
            summary = "Get books by category",
            description = "Retrieve all books belonging to a specific category"
    )
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

    // ========= GET BOOKS IN STOCK =========
    @Operation(
            summary = "Get books in stock",
            description = "Retrieve all books that have stock available"
    )
    @GetMapping("/instock")
    public ResponseEntity<List<BookResponseDto>> getBooksInStock() {
        return ResponseEntity.ok(
                bookService.getBooksInStock()
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    // ========= GET BOOKS BY PRICE =========
    @Operation(
            summary = "Get books by maximum price",
            description = "Retrieve all books with price less than or equal to the given maximum"
    )
    @GetMapping("/price")
    public ResponseEntity<List<BookResponseDto>> getBooksByPrice(@RequestParam Double max) {
        return ResponseEntity.ok(
                bookService.getBooksByPrice(max)
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    // ========= GET BOOKS BY RATING =========
    @Operation(
            summary = "Get books by rating",
            description = "Retrieve all books with average rating greater than or equal to the given value (1–5)"
    )
    @GetMapping("/rating")
    public ResponseEntity<List<BookResponseDto>> getBooksByRating(
            @RequestParam Double minRating) {

        return ResponseEntity.ok(
                bookService.getBooksByRating(minRating)
                        .stream()
                        .map(BookMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }
}
