package com.bookstore.dto.request;

import java.util.List;

public class BookRequestDto {

    private String title;
    private String isbn;
    private Double price;
    private Integer stock;
    private String description;

    // List of author IDs (existing authors)
    private List<Long> authorIds;

    // List of category IDs (existing categories)
    private List<Long> categoryIds;

    public BookRequestDto() {}

    public BookRequestDto(String title, String isbn, Double price, Integer stock, String description, List<Long> authorIds, List<Long> categoryIds) {
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.authorIds = authorIds;
        this.categoryIds = categoryIds;
    }

    // Getters and setters
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

    public List<Long> getAuthorIds() { return authorIds; }
    public void setAuthorIds(List<Long> authorIds) { this.authorIds = authorIds; }

    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
}
