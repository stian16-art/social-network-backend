package com.chanak.social.dto;

import java.time.Instant;

public class PostResponse {

    private Long id;
    private Long authorId;
    private String authorUsername;
    private String authorDisplayName;
    private String content;
    private String imageUrl;
    private Instant createdAt;
    private long likeCount;
    private long commentCount;
    private boolean likedByMe;

    public PostResponse(Long id, Long authorId, String authorUsername, String authorDisplayName,
                         String content, String imageUrl, Instant createdAt,
                         long likeCount, long commentCount, boolean likedByMe) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.authorDisplayName = authorDisplayName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.likedByMe = likedByMe;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }
}
