package com.chanak.social.dto;

import java.time.Instant;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String displayName;
    private Instant joinedAt;

    public UserProfileResponse(Long id, String username, String displayName, Instant joinedAt) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.joinedAt = joinedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }
}
