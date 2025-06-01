package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;

public interface FeedbackUseCase {

    /**
     * 오류 신고 등록
     */
    void createFeedbackUseCase(CreateFeedbackCommand command);
}
