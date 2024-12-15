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
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/feedback")
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

        return ResponseEntity.ok(FeedbackResponse.USER_FEEDBACK_CREATE_FEEDBACK.toResponseDto());
    }
}
