package com.chanak.social.service;

import com.chanak.social.dto.FriendRequestSummary;
import com.chanak.social.dto.FriendStatusResponse;
import com.chanak.social.model.FriendRequest;
import com.chanak.social.model.FriendRequestStatus;
import com.chanak.social.model.User;
import com.chanak.social.repository.FriendRequestRepository;
import com.chanak.social.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public FriendService(FriendRequestRepository friendRequestRepository,
                          UserRepository userRepository,
                          NotificationService notificationService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public FriendStatusResponse getStatus(String currentUsername, Long otherUserId) {
        User currentUser = getUserOrThrow(currentUsername);
        User otherUser = getUserByIdOrThrow(otherUserId);

        if (currentUser.getId().equals(otherUser.getId())) {
            return new FriendStatusResponse("SELF", null);
        }

        if (friendRequestRepository.areFriends(currentUser, otherUser)) {
            return new FriendStatusResponse("FRIENDS", null);
        }

        Optional<FriendRequest> sentByMe = friendRequestRepository.findBySenderAndReceiver(currentUser, otherUser);
        if (sentByMe.isPresent() && sentByMe.get().getStatus() == FriendRequestStatus.PENDING) {
            return new FriendStatusResponse("PENDING_SENT", null);
        }

        Optional<FriendRequest> sentToMe = friendRequestRepository.findBySenderAndReceiver(otherUser, currentUser);
        if (sentToMe.isPresent() && sentToMe.get().getStatus() == FriendRequestStatus.PENDING) {
            return new FriendStatusResponse("PENDING_RECEIVED", sentToMe.get().getId());
        }

        return new FriendStatusResponse("NONE", null);
    }

    public List<FriendRequestSummary> getPendingRequests(String username) {
        User currentUser = getUserOrThrow(username);

        return friendRequestRepository
                .findByReceiverAndStatusOrderByCreatedAtDesc(currentUser, FriendRequestStatus.PENDING)
                .stream()
                .map(fr -> new FriendRequestSummary(
                        fr.getId(),
                        fr.getSender().getId(),
                        fr.getSender().getUsername(),
                        fr.getSender().getDisplayName(),
                        fr.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void sendRequest(String fromUsername, Long toUserId) {
        User sender = getUserOrThrow(fromUsername);
        User receiver = getUserByIdOrThrow(toUserId);

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You can't add yourself as a friend");
        }
        if (friendRequestRepository.areFriends(sender, receiver)) {
            throw new IllegalArgumentException("You're already friends");
        }
        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()
                || friendRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            throw new IllegalArgumentException("A friend request already exists between you two");
        }

        FriendRequest request = new FriendRequest(sender, receiver);
        FriendRequest saved = friendRequestRepository.save(request);
        notificationService.notifyFriendRequest(saved);
    }

    public void respondToRequest(String username, Long requestId, boolean accept) {
        User currentUser = getUserOrThrow(username);
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found: " + requestId));

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("This friend request isn't addressed to you");
        }

        if (accept) {
            request.setStatus(FriendRequestStatus.ACCEPTED);
            friendRequestRepository.save(request);
            notificationService.notifyFriendAccepted(request);
        } else {
            friendRequestRepository.delete(request);
        }
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
