package com.bookstore.controller;

import com.bookstore.dto.request.ReviewRequestDto;
import com.bookstore.dto.response.ReviewResponseDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.UnauthorizedException;
import com.bookstore.model.Review;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.ReviewService;
import com.bookstore.util.ReviewMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@Tag(
        name = "Reviews",
        description = "Endpoints for managing book reviews (customers & admins)"
)
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ========= ADD REVIEW =========
    @Operation(
            summary = "Add review to a book",
            description = "Authenticated users can add a review to a book"
    )
    @PostMapping("/addReview/book/{bookId}")
    public ResponseEntity<ReviewResponseDto> addReview(
            @PathVariable Long bookId,
            @RequestBody ReviewRequestDto reviewRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getId();

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        Review savedReview = reviewService.addReview(bookId, userId, review);

        return ResponseEntity.ok(ReviewMapper.toResponseDto(savedReview));
    }

    // ========= GET REVIEWS BY BOOK =========
    @Operation(
            summary = "Get all reviews for a book",
            description = "Retrieve all reviews associated with a specific book"
    )
    @GetMapping("/getReviews/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByBook(bookId)
                .stream()
                .map(ReviewMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    // ========= GET REVIEWS BY USER =========
    @Operation(
            summary = "Get all reviews by a user",
            description = "Retrieve all reviews written by a specific user"
    )
    @GetMapping("/getReviews/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId)
                .stream()
                .map(ReviewMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    // ========= GET REVIEW BY BOOK AND USER =========
    @Operation(
            summary = "Get a review by book and user",
            description = "Retrieve a review for a specific book by a specific user"
    )
    @GetMapping("/getReview/book/{bookId}/user/{userId}")
    public ResponseEntity<Optional<ReviewResponseDto>> getReviewByBookAndUser(
            @PathVariable Long bookId,
            @PathVariable Long userId) {

        Optional<Review> reviewOpt = reviewService.getReviewByBookAndUser(bookId, userId);
        return ResponseEntity.ok(reviewOpt.map(ReviewMapper::toResponseDto));
    }

    // ========= UPDATE REVIEW =========
    @Operation(
            summary = "Update a review",
            description = "Users can update their own review for a book"
    )
    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto reviewRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Review existingReview = reviewService.getReviewById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!existingReview.getUser().getId().equals(userDetails.getId())) {
            throw new UnauthorizedException("You are not allowed to update this review");
        }

        existingReview.setComment(reviewRequest.getComment());
        existingReview.setRating(reviewRequest.getRating());

        Review updatedReview = reviewService.updateReview(reviewId, existingReview);

        return ResponseEntity.ok(ReviewMapper.toResponseDto(updatedReview));
    }

    // ========= DELETE REVIEW =========
    @Operation(
            summary = "Delete a review",
            description = "Users can delete their own reviews. Admins can delete any review."
    )
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Review existingReview = reviewService.getReviewById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        boolean isOwner = existingReview.getUser().getId().equals(userDetails.getId());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this review");
        }

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
