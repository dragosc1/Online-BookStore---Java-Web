package com.bookstore.controller.admin;

import com.bookstore.dto.request.BookRequestDto;
import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/books")
@PreAuthorize("hasRole('ADMIN')")
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

    // ========= ADMIN ENDPOINTS =========

    @PostMapping("createBook")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto dto) {
        Book book = BookMapper.toEntity(dto, authorService, categoryService);
        Book savedBook = bookService.saveBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookMapper.toResponseDto(savedBook));
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @RequestBody BookRequestDto dto) {

        Optional<Book> bookOpt = bookService.getBookById(id);
        if (bookOpt.isEmpty()) return ResponseEntity.notFound().build();

        Book book = bookOpt.get();
        BookMapper.updateEntity(book, dto, authorService, categoryService);

        return ResponseEntity.ok(
                BookMapper.toResponseDto(bookService.saveBook(book))
        );
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
