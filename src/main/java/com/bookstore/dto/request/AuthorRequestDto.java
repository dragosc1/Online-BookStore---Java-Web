package com.bookstore.dto.request;

public class AuthorRequestDto {
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
