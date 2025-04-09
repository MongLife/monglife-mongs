package com.monglife.mongs.adapter.in.member.web.feedback.controller;

import com.monglife.mongs.application.member.port.in.FeedbackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackUseCase feedbackUseCase;
}
