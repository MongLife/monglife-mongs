package com.monglife.mongs.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Feedback {

    private final Long feedbackId;

    private final Long accountId;

    private final String deviceId;

    private final String deviceName;

    private final String title;

    private final String content;

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
