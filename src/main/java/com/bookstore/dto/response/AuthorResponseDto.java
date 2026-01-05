package com.bookstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing an author")
public class AuthorResponseDto {

    @Schema(
            description = "Unique identifier of the author",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Author full name",
            example = "J.K. Rowling"
    )
    private String name;

    public AuthorResponseDto() {}

    public AuthorResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
