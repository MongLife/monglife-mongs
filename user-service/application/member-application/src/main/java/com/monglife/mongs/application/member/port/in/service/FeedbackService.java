package com.monglife.mongs.application.member.port.in.service;

import com.monglife.mongs.application.member.port.exception.InvalidCreateFeedbackException;
import com.monglife.mongs.application.member.port.in.FeedbackUseCase;
import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;
import com.monglife.mongs.application.member.port.out.FeedbackPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateFeedbackVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService implements FeedbackUseCase {

    private final FeedbackPersistencePort feedbackPersistencePort;

    /**
     * 오류 신고 등록
     */
    @Override
    @Transactional
    public void createFeedbackUseCase(CreateFeedbackCommand command) {

        feedbackPersistencePort.createFeedbackPort(CreateFeedbackVo.builder()
                .accountId(command.getAccountId())
                .deviceId(command.getDeviceId())
                .deviceName(command.getDeviceName())
                .title(command.getTitle())
                .content(command.getContent())
                .build())
                .orElseThrow(InvalidCreateFeedbackException::new);
    }
}
