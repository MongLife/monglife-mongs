package com.monglife.mongs.application.member.port.service;

import com.monglife.mongs.application.member.port.command.CreateFeedbackCommand;
import com.monglife.mongs.application.member.port.in.FeedbackUseCase;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService implements FeedbackUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    public void createFeedbackUseCase(CreateFeedbackCommand createFeedbackCommand) {

    }
}
