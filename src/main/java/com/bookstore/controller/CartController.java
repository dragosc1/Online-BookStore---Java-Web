package com.bookstore.controller;

import com.bookstore.dto.request.CartItemRequestDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    /**
     * Get a cart
     * - Customers see their own cart
     * - Admins can specify userId to get any user's cart
     */
    @GetMapping
    public CartResponseDto getCart(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return cartService.getCart(targetUserId, user);
    }

    /**
     * Add or update a cart item
     * - Customers can only modify their own cart
     * - Admins can specify userId to modify any user's cart
     */
    @PostMapping("/addOrUpdate/item")
    public CartResponseDto addOrUpdateCartItem(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestBody CartItemRequestDto dto
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return cartService.addOrUpdateCartItem(targetUserId, user, dto);
    }

    /**
     * Remove a cart item
     * - Customers can only remove from their own cart
     * - Admins can specify userId to remove items from any user's cart
     */
    @DeleteMapping("/removeCartItem/item/{bookId}")
    public CartResponseDto removeCartItem(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long bookId,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return cartService.removeCartItem(targetUserId, user, bookId);
    }
}
