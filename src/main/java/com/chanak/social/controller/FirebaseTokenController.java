package com.chanak.social.controller;

import com.chanak.social.dto.FirebaseTokenResponse;
import com.chanak.social.model.User;
import com.chanak.social.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/firebase")
public class FirebaseTokenController {

    private final UserRepository userRepository;

    public FirebaseTokenController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(Authentication auth) {
        try {
            User user = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + auth.getName()));

            // Ginagamit ang parehong numeric ID na ginagamit na natin sa
            // Postgres bilang "uid" sa Firebase, para magkatugma ang dalawang
            // sistema nang walang kailangang ihiwalay na mapping table.
            String token = FirebaseAuth.getInstance().createCustomToken(user.getId().toString());

            return ResponseEntity.ok(new FirebaseTokenResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Couldn't issue Firebase token: " + e.getMessage()));
        }
    }
}
