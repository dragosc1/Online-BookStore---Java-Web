package com.bookstore.service.impl;

import com.bookstore.dto.request.CartItemRequestDto;
import com.bookstore.dto.response.CartResponseDto;
import com.bookstore.exception.CartOperationException;
import com.bookstore.exception.InsufficientStockException;
import com.bookstore.exception.UnauthorizedException;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // ------------------ Get Cart ------------------
    @Override
    public CartResponseDto getCart(Long requestedUserId, User currentUser) {
        validateCartAccess(requestedUserId, currentUser);

        User user = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        List<CartResponseDto.CartItemDto> items = cart.getItems().stream()
                .map(item -> new CartResponseDto.CartItemDto(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getBook().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), user.getId(), items);
    }

    // ------------------ Add / Update Cart Item ------------------
    @Override
    @Transactional
    public CartResponseDto addOrUpdateCartItem(Long requestedUserId, User currentUser, CartItemRequestDto dto) {
        validateCartAccess(requestedUserId, currentUser);

        User user = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndBook(cart, book);

        int requestedQuantity = dto.getQuantity();

        if (requestedQuantity > book.getStock()) {
            throw new InsufficientStockException(
                    "Only " + book.getStock() + " items available for book: " + book.getTitle()
            );
        }

        CartItem item;
        if (existingItemOpt.isPresent()) {
            item = existingItemOpt.get();
            item.setQuantity(dto.getQuantity()); // update quantity
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setBook(book);
            item.setQuantity(dto.getQuantity());
        }

        cartItemRepository.save(item);

        return getCart(requestedUserId, currentUser);
    }

    @Override
    @Transactional
    public CartResponseDto removeCartItem(
            Long requestedUserId,
            User currentUser,
            Long bookId
    ) {
        validateCartAccess(requestedUserId, currentUser);

        User user = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new CartOperationException("Item not found in cart"));

        cart.getItems().remove(item);

        return getCart(requestedUserId, currentUser);
    }


    private void validateCartAccess(Long requestedUserId, User currentUser) {
        if (currentUser.getRole().name().equals("CUSTOMER")
                && !requestedUserId.equals(currentUser.getId())) {
            throw new UnauthorizedException("Access denied");
        }
    }
}

