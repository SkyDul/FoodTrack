package com.foodtrack.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter


/**
 * Entitas atau model data untuk BaseEntity.
 * Digunakan untuk merepresentasikan struktur tabel di database.
 */
// [OOP: Abstraction / Abstraksi] 
// Keyword 'abstract' membatasi agar class BaseEntity ini tidak bisa dibuat wujud objeknya (instansiasi).
// Class ini hanya bertugas menjadi "kerangka/blueprint" yang field-nya akan diturunkan.
public abstract class BaseEntity {
    
    // [OOP: Encapsulation / Enkapsulasi] (Terdapat di seluruh Entity)
    // Variabel bertipe private, state disembunyikan. 
    // Akses baca/tulis diurus oleh Getter dan Setter (dibuat otomatis oleh Lombok @Getter @Setter).
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

