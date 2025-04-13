package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;

public interface FeedbackUseCase {

    void createFeedbackUseCase(CreateFeedbackCommand command);
}
