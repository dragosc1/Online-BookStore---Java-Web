package com.bookstore.service;

import com.bookstore.dto.request.CartItemRequestDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.model.User;

public interface CartService {

    /**
     * Get cart for a user
     * @param requestedUserId - the ID of the user whose cart is requested
     * @param currentUser - currently logged-in user (to check access)
     * @return CartResponseDto
     */
    CartResponseDto getCart(Long requestedUserId, User currentUser);

    /**
     * Add or update a cart item
     * @param requestedUserId - the ID of the user whose cart is updated
     * @param currentUser - currently logged-in user (to check access)
     * @param dto - cart item details
     * @return CartResponseDto
     */
    CartResponseDto addOrUpdateCartItem(Long requestedUserId, User currentUser, CartItemRequestDto dto);

    /**
     * Remove an item from a cart
     * @param requestedUserId - the ID of the user whose cart is updated
     * @param currentUser - currently logged-in user (to check access)
     * @param bookId - the book to remove
     * @return CartResponseDto
     */
    CartResponseDto removeCartItem(Long requestedUserId, User currentUser, Long bookId);
}
