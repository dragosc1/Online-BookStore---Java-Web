package com.bookstore.controller;

import com.bookstore.dto.request.CheckoutRequestDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // ------------------ Checkout ------------------
    @PostMapping("/checkout")
    public OrderResponseDto checkout(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestBody CheckoutRequestDto request
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return orderService.checkout(request, user);
    }

    // ------------------ List Orders ------------------
    @GetMapping
    public List<OrderResponseDto> getOrders(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId // optional for admin
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return orderService.getOrders(targetUserId, user);
    }

    // ------------------ Get Single Order ------------------
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long orderId
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return orderService.getOrder(orderId, user);
    }

    // ------------------ Update Order Status ------------------
    @PutMapping("/updateStatus/{orderId}")
    public OrderResponseDto updateStatus(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long orderId,
            @RequestParam String status
    ) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return orderService.updateOrderStatus(orderId, status, user);
    }
}
