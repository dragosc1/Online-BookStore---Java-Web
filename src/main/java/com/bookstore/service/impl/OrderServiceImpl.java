package com.bookstore.service.impl;

import com.bookstore.dto.request.CheckoutRequestDto;
import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.dto.response.OrderResponseDto.OrderItemDto;
import com.bookstore.model.*;
import com.bookstore.model.enums.OrderStatus;
import com.bookstore.model.enums.PaymentStatus;
import com.bookstore.repository.*;
import com.bookstore.service.InventoryService;
import com.bookstore.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final InventoryService inventoryService;

    public OrderServiceImpl(CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            OrderRepository orderRepository,
                            UserRepository userRepository,
                            AddressRepository addressRepository,
                            InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.inventoryService = inventoryService;
    }

    // ---------------- CHECKOUT --------------------
    @Override
    @Transactional
    public OrderResponseDto checkout(CheckoutRequestDto request, User currentUser) {

        Long userId = request.getUserId() != null
                ? request.getUserId()
                : currentUser.getId();

        // --- Role check ---
        if (currentUser.getRole().name().equals("CUSTOMER")
                && !userId.equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: cannot checkout for another user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart does not belong to user");
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // --- Create order ---
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);

        Address address = new Address();
        address.setStreet(request.getShippingAddress().getStreet());
        address.setCity(request.getShippingAddress().getCity());
        address.setState(request.getShippingAddress().getState());
        address.setPostalCode(request.getShippingAddress().getPostalCode());
        address.setCountry(request.getShippingAddress().getCountry());
        address.setUser(user);

        addressRepository.save(address);

        order.setShippingAddress(address);

        // --- Convert CartItems -> OrderItems ---
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        // --- Inventory ---
        inventoryService.checkStock(orderItems);   // validate
        inventoryService.reduceStock(orderItems);  // mutate

        order.setOrderItems(orderItems);

        // --- Payment ---
        Payment payment = new Payment();
        payment.setOrder(order);

        PaymentStatus paymentStatus =
                Math.random() < 0.9 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        payment.setStatus(paymentStatus);

        if (paymentStatus == PaymentStatus.FAILED) {
            inventoryService.restoreStock(orderItems);
            throw new RuntimeException("Payment failed");
        }

        order.setPayment(payment);
        order.setStatus(OrderStatus.PAID);

        orderRepository.save(order);

        // --- Clear cart ---
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear(); // keep in-memory state consistent

        return mapToDto(order);
    }

    // ------------------ List Orders ------------------
    @Override
    public List<OrderResponseDto> getOrders(Long requestedUserId, User currentUser) {
        if (currentUser.getRole().name().equals("CUSTOMER") && !requestedUserId.equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: cannot view other users' orders");
        }

        User user = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // ------------------ Get Single Order ------------------
    @Override
    public OrderResponseDto getOrder(Long orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (currentUser.getRole().name().equals("CUSTOMER") &&
                !order.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: cannot view other users' orders");
        }

        return mapToDto(order);
    }

    // ------------------ Update Order Status ------------------
    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, String status, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (currentUser.getRole().name().equals("CUSTOMER") &&
                !order.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);

        // Restore stock if order is cancelled
        if (newStatus == OrderStatus.CANCELLED) {
            inventoryService.restoreStock(order.getOrderItems());
        }

        orderRepository.save(order);
        return mapToDto(order);
    }

    // ------------------ Mapper ------------------
    private OrderResponseDto mapToDto(Order order) {
        List<OrderItemDto> items = order.getOrderItems().stream()
                .map(item -> new OrderItemDto(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                items,
                order.getShippingAddress().toString(),
                total
        );
    }
}
