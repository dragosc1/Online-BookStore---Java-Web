package com.bookstore.dto.response;

import com.bookstore.model.enums.OrderStatus;
import java.util.List;

public class OrderResponseDto {

    private Long orderId;
    private Long userId;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private String shippingAddress;
    private Double totalAmount;

    public OrderResponseDto() {}

    public OrderResponseDto(Long orderId, Long userId, OrderStatus status, List<OrderItemDto> items, String shippingAddress, Double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static class OrderItemDto {
        private Long bookId;
        private String title;
        private Double price;
        private Integer quantity;

        public OrderItemDto() {}

        public OrderItemDto(Long bookId, String title, Double price, Integer quantity) {
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
