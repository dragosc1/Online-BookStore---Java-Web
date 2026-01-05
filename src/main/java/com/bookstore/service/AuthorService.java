package com.bookstore.service;

import com.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author saveAuthor(Author author);

    List<Author> getAllAuthors();

    Optional<Author> getAuthorById(Long id);

    Optional<Author> getAuthorByName(String name);

    List<Author> searchAuthorsByName(String keyword);

    void deleteAuthorById(Long id);
}
