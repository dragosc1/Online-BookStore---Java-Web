package com.bookstore.controller;

import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookController bookController;

    private Book book;
    private BookResponseDto bookDto;
    private Author author;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy author
        author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        // Setup dummy category
        category = new Category();
        category.setId(1L);
        category.setName("Category Name");

        // Setup dummy book
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("ISBN12345");
        book.setPrice(100.0);
        book.setStock(5);
        book.setDescription("A test book");
        book.setAuthors(List.of(author));
        book.setCategories(List.of(category));
        book.setOrderItems(List.of());
        book.setCartItems(List.of());
        book.setReviews(List.of());

        // Convert to DTO
        bookDto = BookMapper.toResponseDto(book);
    }

    @Test
    void testGetAllBooks() {
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.getAllBooks();

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());
        assertEquals(1, response.getBody().get(0).getAuthors().size());
        assertEquals("Author Name", response.getBody().get(0).getAuthors().get(0).getName());
        assertEquals(1, response.getBody().get(0).getCategories().size());
        assertEquals("Category Name", response.getBody().get(0).getCategories().get(0).getName());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() {
        when(bookService.getBookById(1L)).thenReturn(book);

        ResponseEntity<BookResponseDto> response = bookController.getBookById(1L);

        assertEquals("Test Book", response.getBody().getTitle());
        assertEquals(1, response.getBody().getAuthors().size());
        assertEquals("Author Name", response.getBody().getAuthors().get(0).getName());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testSearchBooks() {
        when(bookService.searchBooksByTitle("Test")).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.searchBooks("Test");

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());

        verify(bookService, times(1)).searchBooksByTitle("Test");
    }

    @Test
    void testGetBooksByAuthor_Found() {
        when(authorService.getAuthorById(1L)).thenReturn(Optional.of(author));
        when(bookService.getBooksByAuthor(author)).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksByAuthor(1L);

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());

        verify(authorService, times(1)).getAuthorById(1L);
        verify(bookService, times(1)).getBooksByAuthor(author);
    }

    @Test
    void testGetBooksByAuthor_NotFound() {
        when(authorService.getAuthorById(1L)).thenReturn(Optional.empty());

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksByAuthor(1L);

        assertEquals(404, response.getStatusCodeValue());

        verify(authorService, times(1)).getAuthorById(1L);
        verify(bookService, never()).getBooksByAuthor(any());
    }

    @Test
    void testGetBooksByCategory_Found() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));
        when(bookService.getBooksByCategory(category)).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksByCategory(1L);

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());

        verify(categoryService, times(1)).getCategoryById(1L);
        verify(bookService, times(1)).getBooksByCategory(category);
    }

    @Test
    void testGetBooksByCategory_NotFound() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksByCategory(1L);

        assertEquals(404, response.getStatusCodeValue());

        verify(categoryService, times(1)).getCategoryById(1L);
        verify(bookService, never()).getBooksByCategory(any());
    }

    @Test
    void testGetBooksInStock() {
        when(bookService.getBooksInStock()).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksInStock();

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());

        verify(bookService, times(1)).getBooksInStock();
    }

    @Test
    void testGetBooksByPrice() {
        when(bookService.getBooksByPrice(150.0)).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDto>> response = bookController.getBooksByPrice(150.0);

        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());

        verify(bookService, times(1)).getBooksByPrice(150.0);
    }
}
