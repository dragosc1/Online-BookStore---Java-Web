package com.bookstore.controller.admin;

import com.bookstore.dto.request.AuthorRequestDto;
import com.bookstore.dto.response.AuthorResponseDto;
import com.bookstore.model.Author;
import com.bookstore.service.AuthorService;
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
class AdminAuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AdminAuthorController controller;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("George Orwell");
    }

    // ========= CREATE =========
    @Test
    void createAuthor_success() {
        AuthorRequestDto request = new AuthorRequestDto();
        request.setName("George Orwell");

        when(authorService.saveAuthor(any(Author.class)))
                .thenReturn(author);

        ResponseEntity<AuthorResponseDto> response =
                controller.createAuthor(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("George Orwell", response.getBody().getName());

        verify(authorService).saveAuthor(any(Author.class));
    }

    // ========= READ =========
    @Test
    void getAuthorById_found() {
        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.of(author));

        ResponseEntity<AuthorResponseDto> response =
                controller.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("George Orwell", response.getBody().getName());
    }

    @Test
    void getAuthorById_notFound() {
        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<AuthorResponseDto> response =
                controller.getAuthorById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllAuthors_success() {
        when(authorService.getAllAuthors())
                .thenReturn(List.of(author));

        ResponseEntity<List<AuthorResponseDto>> response =
                controller.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("George Orwell", response.getBody().get(0).getName());
    }

    // ========= UPDATE =========
    @Test
    void updateAuthor_success() {
        AuthorRequestDto request = new AuthorRequestDto();
        request.setName("Updated Name");

        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.of(author));
        when(authorService.saveAuthor(any(Author.class)))
                .thenReturn(author);

        ResponseEntity<AuthorResponseDto> response =
                controller.updateAuthor(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void updateAuthor_notFound() {
        AuthorRequestDto request = new AuthorRequestDto();
        request.setName("Doesn't Matter");

        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<AuthorResponseDto> response =
                controller.updateAuthor(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(authorService, never()).saveAuthor(any());
    }

    // ========= DELETE =========
    @Test
    void deleteAuthor_success() {
        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.of(author));

        ResponseEntity<Void> response =
                controller.deleteAuthor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(authorService).deleteAuthorById(1L);
    }

    @Test
    void deleteAuthor_notFound() {
        when(authorService.getAuthorById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<Void> response =
                controller.deleteAuthor(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(authorService, never()).deleteAuthorById(any());
    }
}
