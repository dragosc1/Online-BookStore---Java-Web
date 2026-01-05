package com.bookstore.controller;

import com.bookstore.dto.request.ReviewRequestDto;
import com.bookstore.dto.response.ReviewResponseDto;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.UnauthorizedException;
import com.bookstore.model.Book;
import com.bookstore.model.Review;
import com.bookstore.model.User;
import com.bookstore.security.UserDetailsImpl;
import com.bookstore.service.ReviewService;
import com.bookstore.util.ReviewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController controller;

    private User user;
    private UserDetailsImpl currentUser;
    private Review review;
    private ReviewResponseDto reviewResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        currentUser = new UserDetailsImpl(user);

        // Create a Book for the review
        Book book = new Book();
        book.setId(100L);
        book.setTitle("Spring Boot in Action");

        // Create the Review
        review = new Review();
        review.setId(10L);
        review.setComment("Great book!");
        review.setRating(5);
        review.setUser(user);
        review.setBook(book); // <--- IMPORTANT to avoid NPE

        // Create ReviewResponseDto using the mapper
        reviewResponse = ReviewMapper.toResponseDto(review);
    }

    // ========= ADD REVIEW =========
    @Test
    void addReview_success() {
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment("Great book!");
        request.setRating(5);

        when(reviewService.addReview(eq(100L), eq(1L), any(Review.class)))
                .thenReturn(review);

        ResponseEntity<ReviewResponseDto> response = controller.addReview(100L, request, currentUser);

        assertNotNull(response);
        assertEquals(10L, response.getBody().getId());
        assertEquals("Great book!", response.getBody().getComment());
        verify(reviewService).addReview(eq(100L), eq(1L), any(Review.class));
    }

    // ========= GET REVIEWS BY BOOK =========
    @Test
    void getReviewsByBook_success() {
        when(reviewService.getReviewsByBook(100L)).thenReturn(List.of(review));

        ResponseEntity<List<ReviewResponseDto>> response = controller.getReviewsByBook(100L);

        assertEquals(1, response.getBody().size());
        assertEquals("Great book!", response.getBody().get(0).getComment());
    }

    // ========= GET REVIEWS BY USER =========
    @Test
    void getReviewsByUser_success() {
        when(reviewService.getReviewsByUser(1L)).thenReturn(List.of(review));

        ResponseEntity<List<ReviewResponseDto>> response = controller.getReviewsByUser(1L);

        assertEquals(1, response.getBody().size());
        assertEquals("Great book!", response.getBody().get(0).getComment());
    }

    // ========= GET REVIEW BY BOOK AND USER =========
    @Test
    void getReviewByBookAndUser_success() {
        when(reviewService.getReviewByBookAndUser(100L, 1L)).thenReturn(Optional.of(review));

        ResponseEntity<Optional<ReviewResponseDto>> response = controller.getReviewByBookAndUser(100L, 1L);

        assertTrue(response.getBody().isPresent());
        assertEquals("Great book!", response.getBody().get().getComment());
    }

    // ========= UPDATE REVIEW =========
    @Test
    void updateReview_success() {
        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment("Updated comment");
        request.setRating(4);

        when(reviewService.getReviewById(10L)).thenReturn(Optional.of(review));

        Review updatedReview = new Review();
        updatedReview.setId(review.getId());
        updatedReview.setComment(request.getComment());
        updatedReview.setRating(request.getRating());
        updatedReview.setUser(user);
        updatedReview.setBook(review.getBook());

        when(reviewService.updateReview(eq(10L), any(Review.class))).thenReturn(updatedReview);

        ResponseEntity<ReviewResponseDto> response = controller.updateReview(10L, request, currentUser);

        assertEquals("Updated comment", response.getBody().getComment());
        assertEquals(4, response.getBody().getRating());
        verify(reviewService).updateReview(eq(10L), any(Review.class));
    }

    @Test
    void updateReview_notOwner_throwsUnauthorized() {
        User otherUser = new User();
        otherUser.setId(2L);
        review.setUser(otherUser);

        when(reviewService.getReviewById(10L)).thenReturn(Optional.of(review));

        ReviewRequestDto request = new ReviewRequestDto();
        request.setComment("Hacked comment");
        request.setRating(1);

        assertThrows(UnauthorizedException.class,
                () -> controller.updateReview(10L, request, currentUser));
    }

    // ========= DELETE REVIEW =========
    @Test
    void deleteReview_owner_success() {
        when(reviewService.getReviewById(10L)).thenReturn(Optional.of(review));

        ResponseEntity<Void> response = controller.deleteReview(10L, currentUser);

        assertEquals(204, response.getStatusCodeValue());
        verify(reviewService).deleteReview(10L);
    }

    @Test
    void deleteReview_notOwnerNotAdmin_throwsUnauthorized() {
        User otherUser = new User();
        otherUser.setId(2L);
        review.setUser(otherUser);

        when(reviewService.getReviewById(10L)).thenReturn(Optional.of(review));

        assertThrows(UnauthorizedException.class,
                () -> controller.deleteReview(10L, currentUser));
    }
}
