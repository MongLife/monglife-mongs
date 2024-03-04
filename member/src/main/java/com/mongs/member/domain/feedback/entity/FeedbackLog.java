package com.mongs.member.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @Setter
    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;
}
