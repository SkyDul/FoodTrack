package com.foodtrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)


/**
 * Entitas atau model data untuk Notification.
 * Digunakan untuk merepresentasikan struktur tabel di database.
 */
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String type; // success, info, warning, error
    private String icon; // material icon name
    private boolean isRead = false;
    private String category; // STOK, DISTRIBUSI, PETANI
    private Integer targetId; // null means for all users in that role
    private String targetRole; // ROLE_PETANI, ROLE_ADMIN, ALL
    
    // Helper constructor
    public static Notification create(String message, String type, String icon, String category, Integer targetId, String targetRole) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setType(type);
        n.setIcon(icon);
        n.setCategory(category);
        n.setTargetId(targetId);
        n.setTargetRole(targetRole);
        return n;
    }

    public String getRelativeTime() {
        java.time.Duration duration = java.time.Duration.between(this.getCreatedAt(), java.time.LocalDateTime.now());
        long seconds = duration.getSeconds();
        if (seconds < 60) return "Baru saja";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + " menit yang lalu";
        long hours = minutes / 60;
        if (hours < 24) return hours + " jam yang lalu";
        long days = hours / 24;
        return days + " hari yang lalu";
    }
}

