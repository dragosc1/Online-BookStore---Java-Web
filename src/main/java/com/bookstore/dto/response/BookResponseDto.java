package com.bookstore.dto.response;

import com.bookstore.model.Author;
import com.bookstore.model.Category;

import java.util.List;

public class BookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private Double price;
    private Integer stock;
    private String description;

    // Full author info
    private List<Author> authors;

    // Full category info
    private List<Category> categories;

    public BookResponseDto() {}

    public BookResponseDto(Long id, String title, String isbn, Double price, Integer stock, String description, List<Author> authors, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.authors = authors;
        this.categories = categories;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Author> getAuthors() { return authors; }
    public void setAuthors(List<Author> authors) { this.authors = authors; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
}
