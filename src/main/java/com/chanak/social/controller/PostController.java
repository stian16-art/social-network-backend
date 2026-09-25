package com.chanak.social.controller;

import com.chanak.social.dto.CommentRequest;
import com.chanak.social.dto.CommentResponse;
import com.chanak.social.dto.CreatePostRequest;
import com.chanak.social.dto.LikeResponse;
import com.chanak.social.dto.PostResponse;
import com.chanak.social.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(Authentication auth, @Valid @RequestBody CreatePostRequest request) {
        try {
            PostResponse response = postService.createPost(
                    auth.getName(), request.getContent(), request.getImageUrl());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getFeed(Authentication auth,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> feed = postService.getFeed(auth.getName(), pageable);

        return ResponseEntity.ok(Map.of(
                "posts", feed.getContent(),
                "page", feed.getNumber(),
                "totalPages", feed.getTotalPages(),
                "totalPosts", feed.getTotalElements(),
                "hasNext", feed.hasNext()
        ));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(Authentication auth, @PathVariable Long postId) {
        try {
            LikeResponse response = postService.toggleLike(auth.getName(), postId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        try {
            List<CommentResponse> comments = postService.getComments(postId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(Authentication auth, @PathVariable Long postId,
                                         @Valid @RequestBody CommentRequest request) {
        try {
            CommentResponse response = postService.addComment(auth.getName(), postId, request.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
