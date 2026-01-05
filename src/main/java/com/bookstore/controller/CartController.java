package com.bookstore.controller;

import com.bookstore.dto.request.CartItemRequestDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(
        name = "Cart Management",
        description = "Endpoints for managing the shopping cart for customers and admins"
)
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // ========= GET CART =========
    @Operation(
            summary = "Get cart",
            description = "Retrieve the cart for the current customer or specify a userId for admins"
    )
    @GetMapping
    public CartResponseDto getCart(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return cartService.getCart(targetUserId, user);
    }

    // ========= ADD OR UPDATE CART ITEM =========
    @Operation(
            summary = "Add or update cart item",
            description = "Add a new item to the cart or update quantity. Customers can modify their own cart. Admins can specify a userId."
    )
    @PostMapping("/addOrUpdate/item")
    public CartResponseDto addOrUpdateCartItem(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestBody CartItemRequestDto dto
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return cartService.addOrUpdateCartItem(targetUserId, user, dto);
    }

    // ========= REMOVE CART ITEM =========
    @Operation(
            summary = "Remove cart item",
            description = "Remove an item from the cart. Customers can remove their own items. Admins can specify a userId."
    )
    @DeleteMapping("/removeCartItem/item/{bookId}")
    public CartResponseDto removeCartItem(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @PathVariable Long bookId,
            @RequestParam(value = "userId", required = false) Long userId
    ) {
        Long targetUserId = (userId != null) ? userId : currentUser.getId();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        return cartService.removeCartItem(targetUserId, user, bookId);
    }
}
