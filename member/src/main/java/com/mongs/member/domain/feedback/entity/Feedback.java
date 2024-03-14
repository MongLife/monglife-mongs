package com.mongs.member.domain.feedback.entity;

import com.mongs.core.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
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

    @Builder.Default
    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    List<FeedbackLog> feedbackLogList = new ArrayList<>();

    public void addFeedbackLog(FeedbackLog feedbackLog) {
        this.feedbackLogList.add(feedbackLog);
        feedbackLog.setFeedback(this);
    }
}
