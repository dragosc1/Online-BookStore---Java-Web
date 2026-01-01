package com.bookstore.validation.validators;

import com.bookstore.validation.annotations.ValidISBN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ValidISBN, String> {

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.isEmpty()) return false;

        // Remove dashes and spaces
        String cleanIsbn = isbn.replaceAll("[\\s-]", "");

        // ISBN-10: 10 digits, last can be X
        if (cleanIsbn.matches("\\d{9}[\\dX]")) return true;

        // ISBN-13: 13 digits
        if (cleanIsbn.matches("\\d{13}")) return true;

        return false;
    }
}
