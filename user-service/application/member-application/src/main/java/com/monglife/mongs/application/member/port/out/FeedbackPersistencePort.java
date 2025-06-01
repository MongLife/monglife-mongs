package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.CreateFeedbackVo;
import com.monglife.mongs.domain.member.model.Feedback;

import java.util.Optional;

public interface FeedbackPersistencePort {

    Optional<Feedback> createFeedbackPort(CreateFeedbackVo createFeedbackVo);
}
