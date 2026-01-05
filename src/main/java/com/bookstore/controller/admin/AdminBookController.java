package com.bookstore.controller.admin;

import com.bookstore.dto.request.BookRequestDto;
import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/books")
@PreAuthorize("hasRole('ADMIN')")
@Tag(
        name = "Admin Book Management",
        description = "Admin-only endpoints for managing books"
)
@SecurityRequirement(name = "bearerAuth")
public class AdminBookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public AdminBookController(BookService bookService,
                               AuthorService authorService,
                               CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // ========= CREATE =========
    @Operation(
            summary = "Create book",
            description = "Create a new book with author and category information"
    )
    @PostMapping("/createBook")
    public ResponseEntity<BookResponseDto> createBook(
            @RequestBody BookRequestDto dto) {

        Book book = BookMapper.toEntity(dto, authorService, categoryService);
        Book savedBook = bookService.saveBook(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookMapper.toResponseDto(savedBook));
    }

    // ========= UPDATE =========
    @Operation(
            summary = "Update book",
            description = "Update an existing book by its ID"
    )
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequestDto dto) {

        Book book = bookService.getBookById(id);
        BookMapper.updateEntity(book, dto, authorService, categoryService);

        return ResponseEntity.ok(
                BookMapper.toResponseDto(bookService.saveBook(book))
        );
    }

    // ========= DELETE =========
    @Operation(
            summary = "Delete book",
            description = "Delete a book by its ID"
    )
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        // Ensures exception if book does not exist
        bookService.getBookById(id);
        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}
