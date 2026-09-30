package com.chanak.social.controller;

import com.chanak.social.dto.AppVersionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class AppVersionController {

    @Value("${app.version.code}")
    private int versionCode;

    @Value("${app.version.name}")
    private String versionName;

    @Value("${app.version.apk-url}")
    private String apkUrl;

    @Value("${app.version.notes}")
    private String releaseNotes;

    @GetMapping("/version")
    public AppVersionResponse getLatestVersion() {
        return new AppVersionResponse(versionCode, versionName, apkUrl, releaseNotes);
    }
}
