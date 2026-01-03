package com.bookstore.controller;

import com.bookstore.dto.request.ReviewRequestDto;
import com.bookstore.dto.response.ReviewResponseDto;
import com.bookstore.model.Review;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Add review
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

        return ResponseEntity.ok(toResponseDto(savedReview));
    }

    // Get all reviews for a book
    @GetMapping("/getReviews/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByBook(bookId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    // Get all reviews by a user
    @GetMapping("/getReviews/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    // Get review by book and user
    @GetMapping("/getReview/book/{bookId}/user/{userId}")
    public ResponseEntity<Optional<ReviewResponseDto>> getReviewByBookAndUser(
            @PathVariable Long bookId,
            @PathVariable Long userId) {

        Optional<Review> reviewOpt = reviewService.getReviewByBookAndUser(bookId, userId);
        return ResponseEntity.ok(reviewOpt.map(this::toResponseDto));
    }

    // Update review
    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto reviewRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Review existingReview = reviewService.getReviewById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!existingReview.getUser().getId().equals(userDetails.getId())) {
            throw new RuntimeException("You are not allowed to update this review");
        }

        existingReview.setComment(reviewRequest.getComment());
        existingReview.setRating(reviewRequest.getRating());

        Review updatedReview = reviewService.updateReview(reviewId, existingReview);

        return ResponseEntity.ok(toResponseDto(updatedReview));
    }

    // Delete review
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Review existingReview = reviewService.getReviewById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        boolean isOwner = existingReview.getUser().getId().equals(userDetails.getId());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to delete this review");
        }

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // Helper: convert Review -> ReviewResponseDto
    private ReviewResponseDto toResponseDto(Review review) {
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
