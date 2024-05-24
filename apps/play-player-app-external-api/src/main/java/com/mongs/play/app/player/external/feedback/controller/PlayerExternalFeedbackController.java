package com.mongs.play.app.player.external.feedback.controller;

import com.mongs.play.app.player.external.feedback.dto.req.RegisterFeedbackReqDto;
import com.mongs.play.app.player.external.feedback.dto.res.RegisterFeedbackResDto;
import com.mongs.play.app.player.external.feedback.service.PlayerExternalFeedbackService;
import com.mongs.play.app.player.external.feedback.vo.FeedbackLogVo;
import com.mongs.play.app.player.external.feedback.vo.FeedbackVo;
import com.mongs.play.app.player.external.feedback.vo.RegisterFeedbackVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/feedback")
@RequiredArgsConstructor
@RestController
public class PlayerExternalFeedbackController {

    private final PlayerExternalFeedbackService playerExternalFeedbackService;

    @PostMapping("")
    public ResponseEntity<RegisterFeedbackResDto> registerFeedback(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody RegisterFeedbackReqDto registerFeedbackReqDto
    ) {

        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();

        FeedbackVo feedbackVo = registerFeedbackReqDto.feedback();
        List<FeedbackLogVo> feedbackLogVoList = registerFeedbackReqDto.feedbackLogList();

        RegisterFeedbackVo registerFeedbackVo =
                playerExternalFeedbackService.registerFeedback(accountId, deviceId, feedbackVo, feedbackLogVoList);


        return ResponseEntity.ok().body(RegisterFeedbackResDto.builder()
                .id(registerFeedbackVo.id())
                .accountId(registerFeedbackVo.accountId())
                .deviceId(registerFeedbackVo.deviceId())
                .build());
    }

}
