package com.bookstore.service;

import com.bookstore.dto.request.CheckoutRequestDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.model.User;

import java.util.List;

public interface OrderService {

    // Checkout: convert cart into order
    OrderResponseDto checkout(CheckoutRequestDto request, User currentUser);

    // Get all orders for a user (or all if admin)
    List<OrderResponseDto> getOrders(Long requestedUserId, User currentUser);

    // Get a single order
    OrderResponseDto getOrder(Long orderId, User currentUser);

    // Update order status (for admin or tracking)
    OrderResponseDto updateOrderStatus(Long orderId, String status, User currentUser);
}
