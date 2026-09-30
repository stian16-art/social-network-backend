package com.chanak.social.dto;

import java.time.Instant;

public class NotificationResponse {

    private Long id;
    private String type;
    private Long actorId;
    private String actorUsername;
    private String actorDisplayName;
    private Long postId;
    private String postContentSnippet;
    private Long friendRequestId;
    private boolean read;
    private Instant createdAt;

    public NotificationResponse(Long id, String type, Long actorId, String actorUsername,
                                 String actorDisplayName, Long postId, String postContentSnippet,
                                 Long friendRequestId, boolean read, Instant createdAt) {
        this.id = id;
        this.type = type;
        this.actorId = actorId;
        this.actorUsername = actorUsername;
        this.actorDisplayName = actorDisplayName;
        this.postId = postId;
        this.postContentSnippet = postContentSnippet;
        this.friendRequestId = friendRequestId;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Long getActorId() {
        return actorId;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public String getActorDisplayName() {
        return actorDisplayName;
    }

    public Long getPostId() {
        return postId;
    }

    public String getPostContentSnippet() {
        return postContentSnippet;
    }

    public Long getFriendRequestId() {
        return friendRequestId;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
