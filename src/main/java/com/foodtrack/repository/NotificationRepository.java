package com.foodtrack.repository;

import com.foodtrack.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



/**
 * Repository interface untuk mengelola operasi database (CRUD)
 * pada data NotificationRepository.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT n FROM Notification n WHERE " +
           "(n.targetRole = 'ALL' OR (n.targetId = :targetId AND n.targetRole = :targetRole)) " +
           "AND (n.isRead = false OR n.createdAt > :cutoff) " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findRecentByTarget(Integer targetId, String targetRole, java.time.LocalDateTime cutoff);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(n) FROM Notification n WHERE " +
           "(n.targetRole = 'ALL' OR (n.targetId = :targetId AND n.targetRole = :targetRole)) " +
           "AND n.isRead = false")
    long countUnreadByTarget(Integer targetId, String targetRole);

    List<Notification> findByIsReadFalseOrCreatedAtAfterOrderByCreatedAtDesc(java.time.LocalDateTime cutoff);
    long countByIsReadFalse();
}

