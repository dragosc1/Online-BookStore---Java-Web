package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to add or update a cart item")
public class CartItemRequestDto {

    @Schema(description = "ID of the book to add or update in the cart", example = "1", required = true)
    private Long bookId;

    @Schema(description = "Quantity of the book to add or update", example = "2", required = true)
    private Integer quantity;

    public CartItemRequestDto() {}

    public CartItemRequestDto(Long bookId, Integer quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
