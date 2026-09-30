package com.chanak.social.repository;

import com.chanak.social.model.FriendRequest;
import com.chanak.social.model.FriendRequestStatus;
import com.chanak.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender = :user OR fr.receiver = :user) AND fr.status = :status")
    List<FriendRequest> findAllInvolvingUserWithStatus(@Param("user") User user, @Param("status") FriendRequestStatus status);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);

    @Query("SELECT CASE WHEN COUNT(fr) > 0 THEN true ELSE false END FROM FriendRequest fr " +
            "WHERE fr.status = com.chanak.social.model.FriendRequestStatus.ACCEPTED " +
            "AND ((fr.sender = :a AND fr.receiver = :b) OR (fr.sender = :b AND fr.receiver = :a))")
    boolean areFriends(@Param("a") User a, @Param("b") User b);
}
