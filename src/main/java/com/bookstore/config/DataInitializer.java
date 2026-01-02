package com.bookstore.config;

import com.bookstore.model.*;
import com.bookstore.model.enums.Role;
import com.bookstore.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            AuthorRepository authorRepo,
            CategoryRepository categoryRepo,
            BookRepository bookRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ================== Categories ==================
            Category fiction = categoryRepo.findByName("Fiction")
                    .orElseGet(() -> categoryRepo.save(new Category(null, "Fiction", null)));

            Category science = categoryRepo.findByName("Science")
                    .orElseGet(() -> categoryRepo.save(new Category(null, "Science", null)));

            Category fantasy = categoryRepo.findByName("Fantasy")
                    .orElseGet(() -> categoryRepo.save(new Category(null, "Fantasy", null)));

            // ================== Authors ==================
            Author jkRowling = authorRepo.findByName("J.K. Rowling")
                    .orElseGet(() -> authorRepo.save(new Author(null, "J.K. Rowling", null)));

            Author stephenHawking = authorRepo.findByName("Stephen Hawking")
                    .orElseGet(() -> authorRepo.save(new Author(null, "Stephen Hawking", null)));

            Author tolkien = authorRepo.findByName("J.R.R. Tolkien")
                    .orElseGet(() -> authorRepo.save(new Author(null, "J.R.R. Tolkien", null)));

            // ================== Books ==================
            if (bookRepo.findByIsbn("9780747532699").isEmpty()) {
                Book harryPotter = new Book();
                harryPotter.setTitle("Harry Potter and the Philosopher's Stone");
                harryPotter.setIsbn("9780747532699");
                harryPotter.setPrice(29.99);
                harryPotter.setStock(15);
                harryPotter.setAuthors(Collections.singletonList(jkRowling));
                harryPotter.setCategories(Arrays.asList(fiction, fantasy));
                bookRepo.save(harryPotter);
            }

            if (bookRepo.findByIsbn("9780553380163").isEmpty()) {
                Book briefHistory = new Book();
                briefHistory.setTitle("A Brief History of Time");
                briefHistory.setIsbn("9780553380163");
                briefHistory.setPrice(35.99);
                briefHistory.setStock(10);
                briefHistory.setAuthors(Collections.singletonList(stephenHawking));
                briefHistory.setCategories(Collections.singletonList(science));
                bookRepo.save(briefHistory);
            }

            if (bookRepo.findByIsbn("9780618640157").isEmpty()) {
                Book lotr = new Book();
                lotr.setTitle("The Lord of the Rings");
                lotr.setIsbn("9780618640157");
                lotr.setPrice(49.99);
                lotr.setStock(5);
                lotr.setAuthors(Collections.singletonList(tolkien));
                lotr.setCategories(Arrays.asList(fiction, fantasy));
                bookRepo.save(lotr);
            }

            // ================== Admin User ==================
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@bookstore.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);
            }

            System.out.println("Initial data loaded successfully!");
        };
    }
}
