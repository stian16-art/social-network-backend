package com.chanak.social.dto;

import java.time.Instant;

public class FriendRequestSummary {

    private Long requestId;
    private Long senderId;
    private String senderUsername;
    private String senderDisplayName;
    private Instant createdAt;

    public FriendRequestSummary(Long requestId, Long senderId, String senderUsername,
                                 String senderDisplayName, Instant createdAt) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderDisplayName = senderDisplayName;
        this.createdAt = createdAt;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
