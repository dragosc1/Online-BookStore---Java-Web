package com.bookstore.service.impl;

import com.bookstore.exception.InsufficientStockException;
import com.bookstore.model.Book;
import com.bookstore.model.OrderItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final BookRepository bookRepository;

    public InventoryServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ------------------ Check Stock ------------------
    @Override
    public void checkStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Book book = item.getBook();
            if (book.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for book: " + book.getTitle()
                );
            }
        }
    }

    // ------------------ Reduce Stock ------------------
    @Override
    @Transactional
    public void reduceStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Book book = item.getBook();
            int newStock = book.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new InsufficientStockException("Insufficient stock for book: " + book.getTitle());
            }
            book.setStock(newStock);
            bookRepository.save(book);
        }
    }

    // ------------------ Restore Stock ------------------
    @Override
    @Transactional
    public void restoreStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepository.save(book);
        }
    }
}
