package com.mongs.member.domain.feedback.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.member.domain.feedback.controller.dto.request.RegisterFeedbackReqDto;
import com.mongs.member.domain.feedback.controller.dto.response.FindFeedbackResDto;
import com.mongs.member.domain.feedback.controller.dto.response.RegisterFeedbackResDto;
import com.mongs.member.domain.feedback.controller.dto.response.SolveFeedbackResDto;
import com.mongs.member.domain.feedback.service.FeedbackService;
import com.mongs.member.domain.feedback.service.vo.FeedbackLogVo;
import com.mongs.member.domain.feedback.service.vo.FeedbackVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("")
    public ResponseEntity<RegisterFeedbackResDto> registerFeedback(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody RegisterFeedbackReqDto registerFeedbackReqDto
    ) {
        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();

        FeedbackVo feedbackVO = registerFeedbackReqDto.feedback();
        List<FeedbackLogVo> feedbackLogVoList = registerFeedbackReqDto.feedbackLogList();

        return ResponseEntity.ok().body(feedbackService.registerFeedback(
                accountId,
                deviceId,
                feedbackVO,
                feedbackLogVoList
        ));
    }
    @GetMapping("/admin/account/{accountId}")
    public ResponseEntity<List<FindFeedbackResDto>> findFeedbackByAccountId(@PathVariable("accountId") Long accountId) {
        return ResponseEntity.ok().body(feedbackService.findFeedbackByAccountId(accountId));
    }
    @PutMapping("/admin/{feedbackId}")
    public ResponseEntity<SolveFeedbackResDto> solveFeedback(@PathVariable("feedbackId") Long feedbackId) {
        return ResponseEntity.ok().body(feedbackService.solveFeedback(feedbackId));
    }
}
