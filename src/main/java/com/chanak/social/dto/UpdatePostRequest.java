package com.chanak.social.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePostRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
