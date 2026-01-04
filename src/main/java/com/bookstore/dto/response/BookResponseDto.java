package com.bookstore.dto.response;

import com.bookstore.model.Author;
import com.bookstore.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response payload representing a book")
public class BookResponseDto {

    @Schema(description = "Book ID", example = "1")
    private Long id;

    @Schema(description = "Book title", example = "Clean Code")
    private String title;

    @Schema(description = "ISBN number", example = "9780132350884")
    private String isbn;

    @Schema(description = "Book price", example = "29.99")
    private Double price;

    @Schema(description = "Available stock", example = "100")
    private Integer stock;

    @Schema(
            description = "Book description",
            example = "A handbook of agile software craftsmanship"
    )
    private String description;

    @Schema(
            description = "List of authors associated with the book"
    )
    private List<Author> authors;

    @Schema(
            description = "List of categories associated with the book"
    )
    private List<Category> categories;

    public BookResponseDto() {}

    public BookResponseDto(Long id, String title, String isbn, Double price,
                           Integer stock, String description,
                           List<Author> authors, List<Category> categories) {
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
