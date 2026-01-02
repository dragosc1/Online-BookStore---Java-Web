package com.bookstore.validation.annotations;

import com.bookstore.validation.validators.StockValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StockValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStock {
    String message() default "Stock must be greater than or equal to 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
