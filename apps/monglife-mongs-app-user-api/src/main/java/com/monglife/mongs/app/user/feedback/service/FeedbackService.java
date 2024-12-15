package com.monglife.mongs.app.user.feedback.service;

import com.monglife.mongs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MemberService memberService;

    @Transactional
    public void createFeedback(Long accountId, String deviceId, String deviceName, String title, String content) {
        memberService.createFeedback(accountId, deviceId, deviceName, title, content);
    }
}
