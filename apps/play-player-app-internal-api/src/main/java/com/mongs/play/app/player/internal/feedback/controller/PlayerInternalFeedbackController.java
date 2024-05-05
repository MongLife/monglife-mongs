package com.mongs.play.app.player.internal.feedback.controller;

import com.mongs.play.app.player.internal.feedback.service.PlayerInternalFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/feedback")
@RequiredArgsConstructor
@RestController
public class PlayerInternalFeedbackController {

    private final PlayerInternalFeedbackService playerInternalFeedbackService;

}
