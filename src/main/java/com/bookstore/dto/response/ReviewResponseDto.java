package com.bookstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing a book review")
public class ReviewResponseDto {

    @Schema(description = "Review ID", example = "1")
    private Long id;

    @Schema(description = "Review comment", example = "Great book!")
    private String comment;

    @Schema(description = "Review rating (1-5)", example = "5")
    private Integer rating;

    @Schema(description = "Book ID this review belongs to", example = "1")
    private Long bookId;

    @Schema(description = "User ID who wrote the review", example = "1")
    private Long userId;

    @Schema(description = "Username of the reviewer", example = "john_doe")
    private String username;

    public ReviewResponseDto(Long id, String comment, Integer rating, Long bookId, Long userId, String username) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.bookId = bookId;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
