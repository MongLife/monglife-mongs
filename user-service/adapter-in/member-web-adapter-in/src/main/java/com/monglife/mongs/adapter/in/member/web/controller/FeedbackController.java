package com.monglife.mongs.adapter.in.member.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.dto.request.CreateFeedbackRequestDto;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInMemberWebResponse;
import com.monglife.mongs.application.member.port.in.FeedbackUseCase;
import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackUseCase feedbackUseCase;

    /**
     * 오류 신고 등록
     */
    @EntryLoggingPoint
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createFeedback(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody CreateFeedbackRequestDto createFeedbackRequestDto
    ) {

        CreateFeedbackCommand command = CreateFeedbackCommand.builder()
                .accountId(passport.getAccountId())
                .deviceId(passport.getDeviceId())
                .deviceName(createFeedbackRequestDto.getDeviceName())
                .title(createFeedbackRequestDto.getTitle())
                .content(createFeedbackRequestDto.getContent())
                .build();

        feedbackUseCase.createFeedbackUseCase(command);

        return ResponseEntity.ok(AdapterInMemberWebResponse.CREATE_FEEDBACK.toResponseDto());
    }
}
