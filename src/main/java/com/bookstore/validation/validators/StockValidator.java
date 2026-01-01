package com.bookstore.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StockValidator implements ConstraintValidator<com.bookstore.validation.annotations.ValidStock, Integer> {

    @Override
    public boolean isValid(Integer stock, ConstraintValidatorContext context) {
        return stock != null && stock >= 0;
    }
}
