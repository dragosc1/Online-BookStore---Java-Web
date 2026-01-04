package com.bookstore.dto.request;

import com.bookstore.validation.annotations.ValidISBN;
import com.bookstore.validation.annotations.ValidStock;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request payload for creating or updating a book")
public class BookRequestDto {

    @Schema(
            description = "Book title",
            example = "Clean Code",
            required = true
    )
    private String title;

    @ValidISBN
    @Schema(
            description = "ISBN number of the book",
            example = "9780132350884",
            required = true
    )
    private String isbn;

    @Schema(
            description = "Book price",
            example = "29.99"
    )
    private Double price;

    @ValidStock
    @Schema(
            description = "Number of books in stock",
            example = "100"
    )
    private Integer stock;

    @Schema(
            description = "Book description",
            example = "A handbook of agile software craftsmanship"
    )
    private String description;

    @Schema(
            description = "List of existing author IDs",
            example = "[1, 2]"
    )
    private List<Long> authorIds;

    @Schema(
            description = "List of existing category IDs",
            example = "[3, 5]"
    )
    private List<Long> categoryIds;

    public BookRequestDto() {}

    public BookRequestDto(String title, String isbn, Double price, Integer stock,
                          String description, List<Long> authorIds, List<Long> categoryIds) {
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
