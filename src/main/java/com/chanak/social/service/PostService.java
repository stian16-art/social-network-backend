package com.chanak.social.service;

import com.chanak.social.dto.CommentResponse;
import com.chanak.social.dto.LikeResponse;
import com.chanak.social.dto.PostResponse;
import com.chanak.social.model.Comment;
import com.chanak.social.model.Post;
import com.chanak.social.model.PostLike;
import com.chanak.social.model.User;
import com.chanak.social.repository.CommentRepository;
import com.chanak.social.repository.PostLikeRepository;
import com.chanak.social.repository.PostRepository;
import com.chanak.social.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                        PostLikeRepository postLikeRepository,
                        CommentRepository commentRepository,
                        UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public PostResponse createPost(String username, String content, String imageUrl) {
        User author = getUserOrThrow(username);
        Post post = new Post(author, content, imageUrl);
        Post saved = postRepository.save(post);
        return toPostResponse(saved, author);
    }

    public Page<PostResponse> getFeed(String username, Pageable pageable) {
        User currentUser = getUserOrThrow(username);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(post -> toPostResponse(post, currentUser));
    }

    public LikeResponse toggleLike(String username, Long postId) {
        User user = getUserOrThrow(username);
        Post post = getPostOrThrow(postId);

        boolean alreadyLiked = postLikeRepository.existsByPostAndUser(post, user);

        if (alreadyLiked) {
            postLikeRepository.deleteByPostAndUser(post, user);
        } else {
            postLikeRepository.save(new PostLike(post, user));
        }

        long likeCount = postLikeRepository.countByPost(post);
        return new LikeResponse(!alreadyLiked, likeCount);
    }

    public CommentResponse addComment(String username, Long postId, String content) {
        User author = getUserOrThrow(username);
        Post post = getPostOrThrow(postId);

        Comment comment = new Comment(post, author, content);
        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                author.getId(),
                author.getUsername(),
                author.getDisplayName(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }

    public List<CommentResponse> getComments(Long postId) {
        Post post = getPostOrThrow(postId);

        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getAuthor().getId(),
                        c.getAuthor().getUsername(),
                        c.getAuthor().getDisplayName(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    private PostResponse toPostResponse(Post post, User currentUser) {
        long likeCount = postLikeRepository.countByPost(post);
        long commentCount = commentRepository.countByPost(post);
        boolean likedByMe = postLikeRepository.existsByPostAndUser(post, currentUser);

        return new PostResponse(
                post.getId(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getDisplayName(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                likeCount,
                commentCount,
                likedByMe
        );
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
    }
}
