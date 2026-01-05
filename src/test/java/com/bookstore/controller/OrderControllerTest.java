package com.bookstore.controller;

import com.bookstore.dto.request.CheckoutRequestDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.dto.response.OrderResponseDto.OrderItemDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.User;
import com.bookstore.model.enums.OrderStatus;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderController controller;

    private User user;
    private UserDetailsImpl currentUser;
    private OrderResponseDto orderResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        currentUser = new UserDetailsImpl(user);

        // Fully populated OrderResponseDto with items
        OrderItemDto item1 = new OrderItemDto(5L, "Spring Boot in Action", 19.99, 2);
        OrderItemDto item2 = new OrderItemDto(6L, "Java Concurrency", 25.0, 1);

        orderResponse = new OrderResponseDto(
                100L,                  // orderId
                1L,                    // userId
                OrderStatus.PLACED,    // status
                List.of(item1, item2), // items
                "123 Main Street",     // shippingAddress
                64.98                  // totalAmount
        );
    }

    // ========= CHECKOUT =========
    @Test
    void checkout_success() {
        CheckoutRequestDto request = new CheckoutRequestDto();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(orderService.checkout(request, user)).thenReturn(orderResponse);

        OrderResponseDto response = controller.checkout(currentUser, request);

        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
        assertEquals(2, response.getItems().size());
        assertEquals(64.98, response.getTotalAmount());
        verify(orderService).checkout(request, user);
    }

    @Test
    void checkout_userNotFound_throwsException() {
        CheckoutRequestDto request = new CheckoutRequestDto();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> controller.checkout(currentUser, request));

        assertEquals("Current user not found", ex.getMessage());
    }

    // ========= GET ORDERS =========
    @Test
    void getOrders_forCurrentUser_success() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(orderService.getOrders(1L, user)).thenReturn(List.of(orderResponse));

        List<OrderResponseDto> orders = controller.getOrders(currentUser, null);

        assertEquals(1, orders.size());
        assertEquals(100L, orders.get(0).getOrderId());
        assertEquals(2, orders.get(0).getItems().size());
        verify(orderService).getOrders(1L, user);
    }

    @Test
    void getOrders_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> controller.getOrders(currentUser, null));

        assertEquals("Current user not found", ex.getMessage());
    }

    @Test
    void getOrders_adminWithUserId_success() {
        Long targetUserId = 2L;
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(orderService.getOrders(targetUserId, user)).thenReturn(List.of(orderResponse));

        List<OrderResponseDto> orders = controller.getOrders(currentUser, targetUserId);

        assertEquals(1, orders.size());
        assertEquals(100L, orders.get(0).getOrderId());
        verify(orderService).getOrders(targetUserId, user);
    }

    // ========= GET SINGLE ORDER =========
    @Test
    void getOrder_success() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(orderService.getOrder(100L, user)).thenReturn(orderResponse);

        OrderResponseDto response = controller.getOrder(currentUser, 100L);

        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
        assertEquals(2, response.getItems().size());
        verify(orderService).getOrder(100L, user);
    }

    @Test
    void getOrder_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> controller.getOrder(currentUser, 100L));

        assertEquals("Current user not found", ex.getMessage());
    }

    // ========= UPDATE ORDER STATUS =========
    @Test
    void updateStatus_success() {
        String newStatus = "SHIPPED";

        // Mock the user repository to return the current user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Create a new OrderResponseDto representing the updated order
        OrderResponseDto updatedOrder = new OrderResponseDto(
                100L,
                user.getId(),
                OrderStatus.SHIPPED, // updated status
                orderResponse.getItems(),
                orderResponse.getShippingAddress(),
                orderResponse.getTotalAmount()
        );

        // Mock the service to return the updated order
        when(orderService.updateOrderStatus(100L, newStatus, user)).thenReturn(updatedOrder);

        // Call the controller method
        OrderResponseDto response = controller.updateStatus(currentUser, 100L, newStatus);

        // Assertions
        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
        assertEquals(OrderStatus.SHIPPED, response.getStatus()); // checks the updated status

        // Verify that the service was called correctly
        verify(orderService).updateOrderStatus(100L, newStatus, user);
    }


    @Test
    void updateStatus_userNotFound_throwsException() {
        String status = "DELIVERED";
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> controller.updateStatus(currentUser, 100L, status));

        assertEquals("Current user not found", ex.getMessage());
    }
}
