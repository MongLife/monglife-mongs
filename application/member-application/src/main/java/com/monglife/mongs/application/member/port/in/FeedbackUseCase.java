package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.command.CreateFeedbackCommand;

public interface FeedbackUseCase {

    void createFeedbackUseCase(CreateFeedbackCommand createFeedbackCommand);
}
