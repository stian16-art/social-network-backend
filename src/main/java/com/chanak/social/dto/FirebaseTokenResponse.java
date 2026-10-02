package com.chanak.social.dto;

public class FirebaseTokenResponse {

    private String token;

    public FirebaseTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
