package com.bookstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response payload representing a user's shopping cart")
public class CartResponseDto {

    @Schema(description = "ID of the cart", example = "1")
    private Long cartId;

    @Schema(description = "ID of the user who owns the cart", example = "1")
    private Long userId;

    @Schema(description = "List of items in the cart")
    private List<CartItemDto> items;

    public CartResponseDto() {}

    public CartResponseDto(Long cartId, Long userId, List<CartItemDto> items) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
    }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    @Schema(description = "Representation of a single item in the cart")
    public static class CartItemDto {

        @Schema(description = "Book ID", example = "1")
        private Long bookId;

        @Schema(description = "Book title", example = "Clean Code")
        private String title;

        @Schema(description = "Book price", example = "29.99")
        private Double price;

        @Schema(description = "Quantity of this book in the cart", example = "2")
        private Integer quantity;

        public CartItemDto() {}

        public CartItemDto(Long bookId, String title, Double price, Integer quantity) {
            this.bookId = bookId;
            this.title = title;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
