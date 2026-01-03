package com.bookstore.dto.response;

public class ReviewResponseDto {

    private Long id;
    private String comment;
    private Integer rating;
    private Long bookId;
    private Long userId;
    private String username; // optional, for client display

    // Constructor
    public ReviewResponseDto(Long id, String comment, Integer rating, Long bookId, Long userId, String username) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.bookId = bookId;
        this.userId = userId;
        this.username = username;
    }

    // Getters and setters
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
