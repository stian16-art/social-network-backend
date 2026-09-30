package com.chanak.social.dto;

public class FriendStatusResponse {

    // Posibleng values: "NONE", "PENDING_SENT", "PENDING_RECEIVED", "FRIENDS"
    private String status;

    // Kailangan lang kapag "PENDING_RECEIVED" - ang id ng request na
    // sasagutin (accept/decline)
    private Long requestId;

    public FriendStatusResponse(String status, Long requestId) {
        this.status = status;
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public Long getRequestId() {
        return requestId;
    }
}
