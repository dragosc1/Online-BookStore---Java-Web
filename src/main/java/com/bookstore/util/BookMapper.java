package com.bookstore.mapper;

import com.bookstore.dto.request.BookRequestDto;
import com.bookstore.dto.response.BookResponseDto;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.service.AuthorService;
import com.bookstore.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BookMapper {

    private BookMapper() {
        // Utility / mapper class
    }

    // ========= ENTITY → RESPONSE DTO =========
    public static BookResponseDto toResponseDto(Book book) {
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

    // ========= REQUEST DTO → NEW ENTITY =========
    public static Book toEntity(
            BookRequestDto dto,
            AuthorService authorService,
            CategoryService categoryService) {

        Book book = new Book();
        updateEntity(book, dto, authorService, categoryService);
        return book;
    }

    // ========= REQUEST DTO → EXISTING ENTITY =========
    public static void updateEntity(
            Book book,
            BookRequestDto dto,
            AuthorService authorService,
            CategoryService categoryService) {

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        book.setDescription(dto.getDescription());

        book.setAuthors(resolveAuthors(dto.getAuthorIds(), authorService));
        book.setCategories(resolveCategories(dto.getCategoryIds(), categoryService));
    }

    // ========= RELATION RESOLUTION =========
    private static List<Author> resolveAuthors(
            List<Long> authorIds,
            AuthorService authorService) {

        return authorIds.stream()
                .map(authorService::getAuthorById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static List<Category> resolveCategories(
            List<Long> categoryIds,
            CategoryService categoryService) {

        return categoryIds.stream()
                .map(categoryService::getCategoryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
