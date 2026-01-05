package com.bookstore.controller.admin;

import com.bookstore.dto.request.BookRequestDto;
import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AdminBookController controller;

    private Book book;
    private Author author;
    private Category category;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("George Orwell");

        category = new Category();
        category.setId(2L);
        category.setName("Dystopian");

        book = new Book();
        book.setId(10L);
        book.setTitle("1984");
        book.setAuthors(List.of(author));
        book.setCategories(List.of(category));
    }

    // ========= CREATE =========
    @Test
    void createBook_success() {
        BookRequestDto request = new BookRequestDto();
        request.setTitle("1984");
        request.setAuthorIds(List.of(1L));
        request.setCategoryIds(List.of(2L));

        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.of(author));
        when(categoryService.getCategoryById(2L))
                .thenReturn(Optional.of(category));
        when(bookService.saveBook(any(Book.class)))
                .thenReturn(book);

        ResponseEntity<BookResponseDto> response =
                controller.createBook(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1984", response.getBody().getTitle());

        verify(bookService).saveBook(any(Book.class));
    }

    // ========= UPDATE =========
    @Test
    void updateBook_success() {
        BookRequestDto request = new BookRequestDto();
        request.setTitle("Animal Farm");
        request.setAuthorIds(List.of(1L));
        request.setCategoryIds(List.of(2L));

        when(bookService.getBookById(10L))
                .thenReturn(book);
        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.of(author));
        when(categoryService.getCategoryById(2L))
                .thenReturn(Optional.of(category));
        when(bookService.saveBook(any(Book.class)))
                .thenReturn(book);

        ResponseEntity<BookResponseDto> response =
                controller.updateBook(10L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Animal Farm", response.getBody().getTitle());

        verify(bookService).getBookById(10L);
        verify(bookService).saveBook(book);
    }

    // ========= DELETE =========
    @Test
    void deleteBook_success() {
        when(bookService.getBookById(10L))
                .thenReturn(book);

        ResponseEntity<Void> response =
                controller.deleteBook(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookService).getBookById(10L);
        verify(bookService).deleteBook(10L);
    }

    // ========= DELETE - NOT FOUND =========
    @Test
    void deleteBook_notFound_throwsException() {
        when(bookService.getBookById(10L))
                .thenThrow(new RuntimeException("Book not found"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> controller.deleteBook(10L)
        );

        assertEquals("Book not found", ex.getMessage());

        verify(bookService).getBookById(10L);
        verify(bookService, never()).deleteBook(any());
    }
}
