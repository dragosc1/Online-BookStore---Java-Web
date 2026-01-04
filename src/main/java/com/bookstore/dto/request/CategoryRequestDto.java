package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for creating or updating a category")
public class CategoryRequestDto {

    @Schema(
            description = "Category name",
            example = "Science Fiction",
            required = true
    )
    private String name;

    public CategoryRequestDto() {}

    public CategoryRequestDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
