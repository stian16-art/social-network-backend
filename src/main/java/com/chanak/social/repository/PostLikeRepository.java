package com.chanak.social.repository;

import com.chanak.social.model.Post;
import com.chanak.social.model.PostLike;
import com.chanak.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);

    long countByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);

    void deleteByPost(Post post);
}
