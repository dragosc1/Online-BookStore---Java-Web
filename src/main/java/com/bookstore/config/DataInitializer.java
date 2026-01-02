package com.bookstore.config;

import com.bookstore.model.*;
import com.bookstore.model.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bookstore.repository.*;

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
            Category fiction = new Category();
            fiction.setName("Fiction");
            categoryRepo.save(fiction);

            Category science = new Category();
            science.setName("Science");
            categoryRepo.save(science);

            Category fantasy = new Category();
            fantasy.setName("Fantasy");
            categoryRepo.save(fantasy);

            // ================== Authors ==================
            Author jkRowling = new Author();
            jkRowling.setName("J.K. Rowling");
            authorRepo.save(jkRowling);

            Author stephenHawking = new Author();
            stephenHawking.setName("Stephen Hawking");
            authorRepo.save(stephenHawking);

            Author tolkien = new Author();
            tolkien.setName("J.R.R. Tolkien");
            authorRepo.save(tolkien);

            // ================== Books ==================
            Book harryPotter = new Book();
            harryPotter.setTitle("Harry Potter and the Philosopher's Stone");
            harryPotter.setIsbn("9780747532699");
            harryPotter.setPrice(29.99);
            harryPotter.setStock(15);
            harryPotter.setAuthors(Collections.singletonList(jkRowling));
            harryPotter.setCategories(Arrays.asList(fiction, fantasy));
            bookRepo.save(harryPotter);

            Book briefHistory = new Book();
            briefHistory.setTitle("A Brief History of Time");
            briefHistory.setIsbn("9780553380163");
            briefHistory.setPrice(35.99);
            briefHistory.setStock(10);
            briefHistory.setAuthors(Collections.singletonList(stephenHawking));
            briefHistory.setCategories(Collections.singletonList(science));
            bookRepo.save(briefHistory);

            Book lotr = new Book();
            lotr.setTitle("The Lord of the Rings");
            lotr.setIsbn("9780618640157");
            lotr.setPrice(49.99);
            lotr.setStock(5);
            lotr.setAuthors(Collections.singletonList(tolkien));
            lotr.setCategories(Arrays.asList(fiction, fantasy));
            bookRepo.save(lotr);

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
