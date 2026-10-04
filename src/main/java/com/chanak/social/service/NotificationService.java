package com.chanak.social.service;

import com.chanak.social.dto.NotificationResponse;
import com.chanak.social.model.FriendRequest;
import com.chanak.social.model.Notification;
import com.chanak.social.model.NotificationType;
import com.chanak.social.model.Post;
import com.chanak.social.model.User;
import com.chanak.social.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final int SNIPPET_LENGTH = 60;

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyLike(Post post, User actor) {
        // Wag mag-notify kung sarili mong post ang nilike mo
        if (post.getAuthor().getId().equals(actor.getId())) {
            return;
        }
        save(post.getAuthor(), actor, NotificationType.LIKE, post, null);
    }

    public void notifyComment(Post post, User actor) {
        if (post.getAuthor().getId().equals(actor.getId())) {
            return;
        }
        save(post.getAuthor(), actor, NotificationType.COMMENT, post, null);
    }

    public void notifyFriendRequest(FriendRequest request) {
        save(request.getReceiver(), request.getSender(), NotificationType.FRIEND_REQUEST, null, request);
    }

    public void notifyFriendAccepted(FriendRequest request) {
        // Ang orihinal na nag-send ng request ang dapat malaman na na-accept na
        save(request.getSender(), request.getReceiver(), NotificationType.FRIEND_ACCEPTED, null, request);
    }

    private void save(User recipient, User actor, NotificationType type, Post post, FriendRequest friendRequest) {
        Notification notification = new Notification(recipient, actor, type, post, friendRequest);
        notificationRepository.save(notification);
    }

    public Page<NotificationResponse> getNotifications(User user, Pageable pageable) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user, pageable)
                .map(this::toResponse);
    }

    public long getUnreadCount(User user) {
        return notificationRepository.countByRecipientAndReadFalse(user);
    }

    @Transactional
    public void markAllRead(User user) {
        notificationRepository.markAllReadForRecipient(user);
    }

    private NotificationResponse toResponse(Notification n) {
        String snippet = null;
        Long postId = null;
        if (n.getPost() != null) {
            postId = n.getPost().getId();
            String content = n.getPost().getContent();
            snippet = content.length() > SNIPPET_LENGTH
                    ? content.substring(0, SNIPPET_LENGTH) + "..."
                    : content;
        }

        Long friendRequestId = n.getFriendRequest() != null ? n.getFriendRequest().getId() : null;

        return new NotificationResponse(
                n.getId(),
                n.getType().name(),
                n.getActor().getId(),
                n.getActor().getUsername(),
                n.getActor().getDisplayName(),
                postId,
                snippet,
                friendRequestId,
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
