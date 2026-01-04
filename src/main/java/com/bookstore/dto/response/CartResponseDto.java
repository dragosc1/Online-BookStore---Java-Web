package com.bookstore.dto.response;

import java.util.List;

public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private List<CartItemDto> items;

    public CartResponseDto() {}

    public CartResponseDto(Long cartId, Long userId, List<CartItemDto> items) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public static class CartItemDto {
        private Long bookId;
        private String title;
        private Double price;
        private Integer quantity;

        public CartItemDto() {}

        public CartItemDto(Long bookId, String title, Double price, Integer quantity) {
            this.bookId = bookId;
            this.title = title;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
