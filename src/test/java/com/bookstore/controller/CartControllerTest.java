package com.bookstore.controller;

import com.bookstore.dto.request.CartItemRequestDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartController controller;

    private User user;
    private UserDetailsImpl currentUser;
    private CartResponseDto cartResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        currentUser = new UserDetailsImpl(user);

        cartResponse = new CartResponseDto();
        // You can populate cartResponse with sample items if needed
    }

    // ========= GET CART =========
    @Test
    void getCart_forCurrentUser_success() {
        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));
        when(cartService.getCart(1L, user))
                .thenReturn(cartResponse);

        CartResponseDto response = controller.getCart(currentUser, null);

        assertNotNull(response);
        verify(userRepository).findById(1L);
        verify(cartService).getCart(1L, user);
    }

    @Test
    void getCart_currentUserNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getCart(currentUser, null)
        );

        assertEquals("Current user not found", ex.getMessage());
    }

    // ========= ADD OR UPDATE CART ITEM =========
    @Test
    void addOrUpdateCartItem_success() {
        CartItemRequestDto request = new CartItemRequestDto();
        request.setBookId(10L);
        request.setQuantity(2);

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));
        when(cartService.addOrUpdateCartItem(1L, user, request))
                .thenReturn(cartResponse);

        CartResponseDto response = controller.addOrUpdateCartItem(currentUser, null, request);

        assertNotNull(response);
        verify(cartService).addOrUpdateCartItem(1L, user, request);
    }

    @Test
    void addOrUpdateCartItem_userNotFound_throwsException() {
        CartItemRequestDto request = new CartItemRequestDto();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.addOrUpdateCartItem(currentUser, null, request)
        );

        assertEquals("Current user not found", ex.getMessage());
    }

    // ========= REMOVE CART ITEM =========
    @Test
    void removeCartItem_success() {
        Long bookId = 10L;

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));
        when(cartService.removeCartItem(1L, user, bookId))
                .thenReturn(cartResponse);

        CartResponseDto response = controller.removeCartItem(currentUser, bookId, null);

        assertNotNull(response);
        verify(cartService).removeCartItem(1L, user, bookId);
    }

    @Test
    void removeCartItem_userNotFound_throwsException() {
        Long bookId = 10L;
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> controller.removeCartItem(currentUser, bookId, null)
        );

        assertEquals("Current user not found", ex.getMessage());
    }

    // ========= GET CART for admin specifying userId =========
    @Test
    void getCart_adminWithUserId_success() {
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(cartService.getCart(targetUserId, user)).thenReturn(cartResponse);

        CartResponseDto response = controller.getCart(currentUser, targetUserId);

        assertNotNull(response);
        verify(cartService).getCart(targetUserId, user);
    }
}
