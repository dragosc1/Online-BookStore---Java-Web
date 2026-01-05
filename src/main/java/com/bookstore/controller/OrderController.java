package com.bookstore.controller;

import com.bookstore.dto.request.CheckoutRequestDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(
        name = "Orders",
        description = "Endpoints for managing orders, checkout, and order status"
)
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // ========= CHECKOUT =========
    @Operation(
            summary = "Checkout cart",
            description = "Customers can checkout their cart to create an order"
    )
    @PostMapping("/checkout")
    public OrderResponseDto checkout(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestBody CheckoutRequestDto request
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return orderService.checkout(request, user);
    }

    // ========= LIST ORDERS =========
    @Operation(
            summary = "Get list of orders",
            description = "Retrieve orders for the current user. Admins can specify a userId to get any user's orders."
    )
    @GetMapping
    public List<OrderResponseDto> getOrders(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return orderService.getOrders(targetUserId, user);
    }

    // ========= GET SINGLE ORDER =========
    @Operation(
            summary = "Get single order",
            description = "Retrieve a single order by orderId for the current user"
    )
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long orderId
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return orderService.getOrder(orderId, user);
    }

    // ========= UPDATE ORDER STATUS =========
    @Operation(
            summary = "Update order status",
            description = "Update the status of an order (e.g., shipped, delivered). Typically restricted to admins."
    )
    @PutMapping("/updateStatus/{orderId}")
    public OrderResponseDto updateStatus(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long orderId,
            @RequestParam String status
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return orderService.updateOrderStatus(orderId, status, user);
    }
}
