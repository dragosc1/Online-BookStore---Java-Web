package com.bookstore.util;

import com.bookstore.dto.response.OrderResponseDto;
import com.bookstore.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    // ------------------ Mapper ------------------
    public static OrderResponseDto mapToDto(Order order) {
        List<OrderResponseDto.OrderItemDto> items = order.getOrderItems().stream()
                .map(item -> new OrderResponseDto.OrderItemDto(
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
