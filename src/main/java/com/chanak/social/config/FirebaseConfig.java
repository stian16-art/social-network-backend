package com.chanak.social.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${app.firebase.credentials-base64:}")
    private String credentialsBase64;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        if (credentialsBase64 == null || credentialsBase64.isBlank()) {
            throw new IllegalStateException(
                    "Missing FIREBASE_CREDENTIALS_BASE64 env var - Firebase features won't work without it.");
        }

        byte[] decoded = Base64.getDecoder().decode(credentialsBase64);

        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decoded));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}
