package com.chanak.social.dto;

public class AppVersionResponse {

    private int versionCode;
    private String versionName;
    private String apkUrl;
    private String releaseNotes;

    public AppVersionResponse(int versionCode, String versionName, String apkUrl, String releaseNotes) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apkUrl = apkUrl;
        this.releaseNotes = releaseNotes;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }
}
