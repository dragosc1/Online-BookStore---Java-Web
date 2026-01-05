package com.bookstore.util;

import com.bookstore.dto.response.ReviewResponseDto;
import com.bookstore.model.Review;

public class ReviewMapper {
    // ========= HELPER: Convert Review -> ReviewResponseDto =========
    public static ReviewResponseDto toResponseDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getComment(),
                review.getRating(),
                review.getBook().getId(),
                review.getUser().getId(),
                review.getUser().getUsername()
        );
    }
}
