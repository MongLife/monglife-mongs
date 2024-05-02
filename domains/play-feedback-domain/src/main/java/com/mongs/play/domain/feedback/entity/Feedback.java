package com.mongs.play.domain.feedback.entity;

import com.mongs.play.domain.core.jpa.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(name = "feedback_id")
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
    private List<FeedbackLog> feedbackLogList = new ArrayList<>();

    public void addFeedbackLog(FeedbackLog feedbackLog) {
        this.feedbackLogList.add(feedbackLog);
        feedbackLog.setFeedback(this);
    }
}
