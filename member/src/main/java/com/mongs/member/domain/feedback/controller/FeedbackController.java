package com.mongs.member.domain.feedback.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.member.domain.feedback.dto.request.RegisterFeedbackReqDto;
import com.mongs.member.domain.feedback.service.FeedbackService;
import com.mongs.member.domain.feedback.vo.FeedbackLogVO;
import com.mongs.member.domain.feedback.vo.FeedbackVO;
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

    @GetMapping("/admin/account")
    public ResponseEntity<Object> findFeedbackByAccountId(@AuthenticationPrincipal PassportDetail passportDetail) {
        Long accountId = passportDetail.getId();
        return ResponseEntity.ok().body(feedbackService.findFeedbackByAccountId(accountId));
    }
    @PostMapping("")
    public ResponseEntity<Object> registerFeedback(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody RegisterFeedbackReqDto registerFeedbackReqDto
    ) {
        Long accountId = passportDetail.getId();
        String deviceId = passportDetail.getDeviceId();

        FeedbackVO feedbackVO = registerFeedbackReqDto.feedback();
        List<FeedbackLogVO> feedbackLogVOList = registerFeedbackReqDto.feedbackLogList();

        return ResponseEntity.ok().body(feedbackService.registerFeedback(
                accountId,
                deviceId,
                feedbackVO,
                feedbackLogVOList
        ));
    }
    @PutMapping("/admin/{feedbackId}")
    public ResponseEntity<Object> solveFeedback(@PathVariable Long feedbackId) {
        return ResponseEntity.ok().body(feedbackService.solveFeedback(feedbackId));
    }
}
