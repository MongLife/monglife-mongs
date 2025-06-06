package com.monglife.mongs.domain.member.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Notice {

    private final Long noticeId;

    private final String title;

    private final String content;

    private final Long writerAccountId;

    private final String writerName;

    private final Boolean isHided;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    @Builder
    public Notice(Long noticeId, String title, String content, Long writerAccountId, String writerName, Boolean isHided, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.writerAccountId = writerAccountId;
        this.writerName = writerName;
        this.isHided = isHided;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
