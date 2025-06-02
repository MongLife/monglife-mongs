package com.monglife.mongs.adapter.in.member.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetNoticeResponseDto {

    private Long noticeId;

    private String title;

    private String content;

    private String writerName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public GetNoticeResponseDto(Long noticeId, String title, String content, String writerName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
