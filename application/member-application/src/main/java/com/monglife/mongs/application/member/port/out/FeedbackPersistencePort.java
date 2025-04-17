package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreateFeedbackVo;
import com.monglife.mongs.domain.model.Feedback;

public interface FeedbackPersistencePort {

    Feedback createFeedbackPort(CreateFeedbackVo createFeedbackVo);
}
