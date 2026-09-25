package com.chanak.social.dto;

import java.time.Instant;

public class CommentResponse {

    private Long id;
    private Long authorId;
    private String authorUsername;
    private String authorDisplayName;
    private String content;
    private Instant createdAt;

    public CommentResponse(Long id, Long authorId, String authorUsername, String authorDisplayName,
                            String content, Instant createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.authorDisplayName = authorDisplayName;
        this.content = content;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }
}
