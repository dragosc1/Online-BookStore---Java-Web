package com.bookstore.dto.response;

import com.bookstore.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response payload representing an order")
public class OrderResponseDto {

    @Schema(description = "Order ID", example = "1")
    private Long orderId;

    @Schema(description = "User ID who placed the order", example = "2")
    private Long userId;

    @Schema(description = "Order status", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "List of items in the order")
    private List<OrderItemDto> items;

    @Schema(description = "Shipping address as a string")
    private String shippingAddress;

    @Schema(description = "Total order amount", example = "59.99")
    private Double totalAmount;

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

    @Schema(description = "Individual item in an order")
    public static class OrderItemDto {
        @Schema(description = "Book ID", example = "5")
        private Long bookId;

        @Schema(description = "Book title", example = "Spring Boot in Action")
        private String title;

        @Schema(description = "Book price", example = "19.99")
        private Double price;

        @Schema(description = "Quantity ordered", example = "2")
        private Integer quantity;

        public OrderItemDto() {
        }

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
