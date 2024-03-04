package com.mongs.member.domain.feedback.entity;

import com.mongs.core.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
public class Feedback extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false, nullable = false)
    private Long accountId;
    @Column(updatable = false, nullable = false)
    private String deviceId;
    @Column(updatable = false, nullable = false)
    private String code;
    @Column(updatable = false, nullable = false)
    private String title;
    @Column(updatable = false, nullable = false)
    private String content;
    @Column(nullable = false)
    @Builder.Default
    private Boolean isSolved = false;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    List<FeedbackLog> feedbackLogList;

    @Override
    public String toString() {
        return String.format("Feedback(id: %d, accountId: %d, deviceId: %s, code: %s, title: %s, content: %s, feedbackLogList: %s)", id, accountId, deviceId, code, title, content, feedbackLogList);
    }
}
