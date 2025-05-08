package com.monglife.mongs.adapter.out.member.persistence.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.domain.model.Feedback;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_feedback")
public class FeedbackEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Builder
    public FeedbackEntity(Long accountId, String deviceId, String deviceName, String title, String content) {
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.title = title;
        this.content = content;
    }

    public Feedback toDomain() {
        return Feedback.builder()
                .feedbackId(this.feedbackId)
                .accountId(this.accountId)
                .deviceId(this.deviceId)
                .deviceName(this.deviceName)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
