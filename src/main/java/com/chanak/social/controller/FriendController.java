package com.chanak.social.controller;

import com.chanak.social.dto.FriendStatusResponse;
import com.chanak.social.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(Authentication auth, @PathVariable Long userId) {
        try {
            FriendStatusResponse response = friendService.getStatus(auth.getName(), userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<?> sendRequest(Authentication auth, @PathVariable Long userId) {
        try {
            friendService.sendRequest(auth.getName(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/respond/{requestId}")
    public ResponseEntity<?> respond(Authentication auth, @PathVariable Long requestId,
                                      @RequestParam boolean accept) {
        try {
            friendService.respondToRequest(auth.getName(), requestId, accept);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
