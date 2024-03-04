package com.mongs.member.domain.feedback.controller;

import com.mongs.member.domain.feedback.dto.request.RegisterFeedbackReqDto;
import com.mongs.member.domain.feedback.service.FeedbackService;
import com.mongs.member.domain.feedback.vo.FeedbackLogVO;
import com.mongs.member.domain.feedback.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/account")
    public ResponseEntity<Object> findFeedbackByAccountId(@RequestParam("accountId") Long accountId) {
        return ResponseEntity.ok().body(feedbackService.findFeedbackByAccountId(accountId));
    }
    @PostMapping("")
    public ResponseEntity<Object> registerFeedback(@RequestBody RegisterFeedbackReqDto registerFeedbackReqDto) {
        FeedbackVO feedbackVO = registerFeedbackReqDto.feedback();
        List<FeedbackLogVO> feedbackLogVOList = registerFeedbackReqDto.feedbackLogList();

        return ResponseEntity.ok().body(feedbackService.registerFeedback(feedbackVO, feedbackLogVOList));
    }
    @PutMapping("/{feedbackId}")
    public ResponseEntity<Object> solveFeedback(@PathVariable Long feedbackId) {
        return ResponseEntity.ok().body(feedbackService.solveFeedback(feedbackId));
    }
}
