package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for creating or updating an author")
public class AuthorRequestDto {

    @Schema(
            description = "Author full name",
            example = "J.K. Rowling",
            required = true
    )
    private String name;

    public AuthorRequestDto() {}

    public AuthorRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
