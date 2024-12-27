package com.monglife.mongs.app.user.feedback.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.feedback.dto.request.CreateFeedbackRequestDto;
import com.monglife.mongs.app.user.feedback.enums.FeedbackResponse;
import com.monglife.mongs.app.user.feedback.service.FeedbackService;
import com.monglife.mongs.module.security.global.principal.Passport;
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

    private final FeedbackService feedbackService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createFeedback(@AuthenticationPrincipal Passport passport, @RequestBody CreateFeedbackRequestDto createFeedbackRequestDto) {

        Long accountId = passport.getAccountId();
        String deviceId = passport.getDeviceId();
        String deviceName = createFeedbackRequestDto.getDeviceName();
        String title = createFeedbackRequestDto.getTitle();
        String content = createFeedbackRequestDto.getContent();

        feedbackService.createFeedback(accountId, deviceId, deviceName, title, content);

        return ResponseEntity.ok(FeedbackResponse.APP_USER_FEEDBACK_CREATE_FEEDBACK.toResponseDto());
    }
}
