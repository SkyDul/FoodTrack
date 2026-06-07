package com.foodtrack.service;

import com.foodtrack.entity.Notification;
import com.foodtrack.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor


/**
 * Kelas Service untuk NotificationService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findRecent(Integer targetId, String targetRole) {
        java.time.LocalDateTime cutoff = java.time.LocalDateTime.now().minusHours(24);
        if ("ROLE_ADMIN".equals(targetRole)) {
            return notificationRepository.findByIsReadFalseOrCreatedAtAfterOrderByCreatedAtDesc(cutoff);
        }
        return notificationRepository.findRecentByTarget(targetId, targetRole, cutoff);
    }

    public long countUnread(Integer targetId, String targetRole) {
        if ("ROLE_ADMIN".equals(targetRole)) {
            return notificationRepository.countByIsReadFalse();
        }
        return notificationRepository.countUnreadByTarget(targetId, targetRole);
    }

    @Transactional
    public void createNotification(String message, String type, String icon, String category, Integer targetId, String targetRole) {
        notificationRepository.save(Notification.create(message, type, icon, category, targetId, targetRole));
    }
    
    // Legacy support or global admin alerts
    @Transactional
    public void createGlobalNotification(String message, String type, String icon, String category) {
        createNotification(message, type, icon, category, null, "ALL");
    }

    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public void markAllAsRead(Integer targetId, String targetRole) {
        List<Notification> unread;
        if ("ROLE_ADMIN".equals(targetRole)) {
            unread = notificationRepository.findAll().stream().filter(n -> !n.isRead()).toList();
        } else {
            java.time.LocalDateTime cutoff = java.time.LocalDateTime.now().minusYears(1); // just any old date
            unread = notificationRepository.findRecentByTarget(targetId, targetRole, cutoff).stream().filter(n -> !n.isRead()).toList();
        }
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }
}

