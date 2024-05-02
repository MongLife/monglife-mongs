package com.mongs.play.app.player.external.feedback.controller;

import com.mongs.play.app.player.external.feedback.service.PlayerExternalFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/feedback")
@RequiredArgsConstructor
@RestController
public class PlayerExternalFeedbackController {

    private final PlayerExternalFeedbackService playerExternalFeedbackService;


}
