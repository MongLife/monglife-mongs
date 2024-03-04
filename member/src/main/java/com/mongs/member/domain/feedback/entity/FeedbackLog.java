package com.mongs.member.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
public class FeedbackLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false, nullable = false)
    private String location;
    @Column(updatable = false, nullable = false)
    private String message;
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @Override
    public String toString() {
        return String.format("FeedbackLog(id: %d, location: %s, message: %s, createdAt: %s)", id, location, message, createdAt.toString());
    }
}
