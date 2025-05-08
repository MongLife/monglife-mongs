package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Feedback {

    private Long feedbackId;

    private Long accountId;

    private String deviceId;

    private String deviceName;

    private String title;

    private String content;

    @Builder
    public Feedback(Long feedbackId, Long accountId, String deviceId, String deviceName, String title, String content) {
        this.feedbackId = feedbackId;
        this.accountId = accountId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.title = title;
        this.content = content;
    }
}
