package com.bookstore.controller;

import com.bookstore.dto.request.BookRequestDto;
import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.model.Book;
import com.bookstore.model.Author;
import com.bookstore.model.Category;
import com.bookstore.service.BookService;
import com.bookstore.service.AuthorService;
import com.bookstore.service.CategoryService;

import org.springframework.http.HttpStatus;
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

    public BookController(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // ====== PUBLIC ENDPOINTS ======

    // GET all books
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> books = bookService.getAllBooks()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // GET book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(b -> ResponseEntity.ok(mapToResponseDTO(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    // SEARCH books by title
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> searchBooks(@RequestParam String title) {
        List<BookResponseDto> books = bookService.searchBooksByTitle(title)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // FILTER books by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        Optional<Author> authorOpt = authorService.getAuthorById(authorId);
        if (authorOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<BookResponseDto> books = bookService.getBooksByAuthor(authorOpt.get())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // FILTER books by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByCategory(@PathVariable Long categoryId) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(categoryId);
        if (categoryOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<BookResponseDto> books = bookService.getBooksByCategory(categoryOpt.get())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // GET books in stock
    @GetMapping("/instock")
    public ResponseEntity<List<BookResponseDto>> getBooksInStock() {
        List<BookResponseDto> books = bookService.getBooksInStock()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // GET books by price
    @GetMapping("/price")
    public ResponseEntity<List<BookResponseDto>> getBooksByPrice(@RequestParam Double max) {
        List<BookResponseDto> books = bookService.getBooksByPrice(max)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // ====== ADMIN ENDPOINTS ======
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto bookRequest) {
        Book book = mapToEntity(bookRequest);
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDTO(savedBook));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookRequest) {
        Optional<Book> existingBookOpt = bookService.getBookById(id);
        if (existingBookOpt.isEmpty()) return ResponseEntity.notFound().build();

        Book book = existingBookOpt.get();
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setPrice(bookRequest.getPrice());
        book.setStock(bookRequest.getStock());
        book.setDescription(bookRequest.getDescription());

        // Map authors & categories
        List<Author> authors = bookRequest.getAuthorIds()
                .stream()
                .map(authorService::getAuthorById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        book.setAuthors(authors);

        List<Category> categories = bookRequest.getCategoryIds()
                .stream()
                .map(categoryService::getCategoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        book.setCategories(categories);

        Book updatedBook = bookService.saveBook(book);
        return ResponseEntity.ok(mapToResponseDTO(updatedBook));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookService.getBookById(id);
        if (existingBook.isEmpty()) return ResponseEntity.notFound().build();

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ====== HELPER METHODS ======

    private BookResponseDto mapToResponseDTO(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getStock(),
                book.getDescription(),
                book.getAuthors(),
                book.getCategories()
        );
    }

    private Book mapToEntity(BookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        book.setDescription(dto.getDescription());

        // Map authors
        List<Author> authors = dto.getAuthorIds()
                .stream()
                .map(authorService::getAuthorById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        book.setAuthors(authors);

        // Map categories
        List<Category> categories = dto.getCategoryIds()
                .stream()
                .map(categoryService::getCategoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        book.setCategories(categories);

        return book;
    }
}
