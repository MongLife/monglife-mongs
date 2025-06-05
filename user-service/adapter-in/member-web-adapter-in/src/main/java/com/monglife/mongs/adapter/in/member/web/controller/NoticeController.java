package com.monglife.mongs.adapter.in.member.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.mongs.adapter.in.member.web.dto.response.GetNoticeResponseDto;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInMemberWebResponse;
import com.monglife.mongs.application.member.port.in.NoticeUseCase;
import com.monglife.mongs.application.member.port.in.command.GetNoticeCommand;
import com.monglife.mongs.application.member.port.in.command.GetNoticesCommand;
import com.monglife.mongs.domain.member.model.Notice;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;

    /**
     * 공지 사항 조회
     * @param noticeId 공지 사항 ID
     */
    @EntryLoggingPoint
    @GetMapping("/{noticeId}")
    public ResponseEntity<ResponseDto<GetNoticeResponseDto>> getNotice(@PathVariable("noticeId") @NotNull @Min(1) Long noticeId) {

        GetNoticeCommand command = GetNoticeCommand.builder()
                .noticeId(noticeId)
                .build();

        Notice notice = noticeUseCase.getNoticeUseCase(command);

        GetNoticeResponseDto getNoticeResponseDto = GetNoticeResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writerName(notice.getWriterName())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_NOTICE.toResponseDto(getNoticeResponseDto));
    }

    /**
     * 공지 사항 목록 조회
     * @param page 페이지
     * @param size 사이즈
     */
    @EntryLoggingPoint
    @GetMapping
    public ResponseEntity<ResponseDto<List<GetNoticeResponseDto>>> getNotices(
            @RequestParam("page") @NotNull @Min(1) Integer page,
            @RequestParam("size") @NotNull @Min(1) @Max(50) Integer size
    ) {

        GetNoticesCommand command = GetNoticesCommand.builder()
                .page(page)
                .size(size)
                .build();

        List<Notice> notices = noticeUseCase.getNoticesUseCase(command);

        List<GetNoticeResponseDto> getNoticeResponseDtos = notices.stream()
                .map(notice -> GetNoticeResponseDto.builder()
                        .noticeId(notice.getNoticeId())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .writerName(notice.getWriterName())
                        .createdAt(notice.getCreatedAt())
                        .updatedAt(notice.getUpdatedAt())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_NOTICES.toResponseDto(getNoticeResponseDtos));
    }
}
