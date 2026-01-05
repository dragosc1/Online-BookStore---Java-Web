package com.bookstore.util;

import com.bookstore.dto.request.AuthorRequestDto;
import com.bookstore.dto.response.AuthorResponseDto;
import com.bookstore.model.Author;

public final class AuthorMapper {

    public static AuthorResponseDto toResponseDto(Author author) {
        if (author == null) return null;
        return new AuthorResponseDto(
                author.getId(),
                author.getName()
        );
    }

    public static Author toEntity(AuthorRequestDto dto) {
        if (dto == null) return null;
        Author author = new Author();
        author.setName(dto.getName());
        return author;
    }

    public static void updateEntity(Author author, AuthorRequestDto dto) {
        if (author == null || dto == null) return;
        author.setName(dto.getName());
    }
}
