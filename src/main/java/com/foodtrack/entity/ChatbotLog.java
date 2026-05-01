package com.foodtrack.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_log")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class ChatbotLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;

    @Column(name = "user_prompt", columnDefinition = "TEXT", nullable = false)
    private String userPrompt;

    @Column(name = "gemini_response", columnDefinition = "TEXT")
    private String geminiResponse;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petani", nullable = false)
    private Petani petani;

    @Override
    public String toString() {
        return "ChatbotLog{id=" + idLog + "}";
    }
}
