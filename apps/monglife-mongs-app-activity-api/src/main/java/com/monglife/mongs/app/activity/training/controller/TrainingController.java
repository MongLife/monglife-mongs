package com.monglife.mongs.app.activity.training.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.activity.training.enums.TrainingResponse;
import com.monglife.mongs.module.security.global.principal.Passport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    @PostMapping("/runner/{mongId}")
    public ResponseEntity<ResponseDto<?>> endRunner(@AuthenticationPrincipal Passport passport, @PathVariable("mongId") String mongId) {

        return ResponseEntity.ok(TrainingResponse.APP_ACTIVITY_TRAINING_END_RUNNER.toResponseDto());
    }
}

