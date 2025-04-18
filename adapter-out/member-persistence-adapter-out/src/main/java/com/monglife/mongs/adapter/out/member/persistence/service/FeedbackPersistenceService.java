package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.entity.FeedbackEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.FeedbackRepository;
import com.monglife.mongs.application.member.port.out.FeedbackPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateFeedbackVo;
import com.monglife.mongs.domain.model.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackPersistenceService implements FeedbackPersistencePort {

    private final FeedbackRepository feedbackRepository;

    /**
     * 오류 신고 등록
     * @return 오류 신고 도메인 객체
     */
    @Override
    @Transactional
    public Optional<Feedback> createFeedbackPort(CreateFeedbackVo createFeedbackVo) {

        FeedbackEntity feedbackEntity = FeedbackEntity.builder()
                .accountId(createFeedbackVo.getAccountId())
                .deviceId(createFeedbackVo.getDeviceId())
                .deviceName(createFeedbackVo.getDeviceName())
                .title(createFeedbackVo.getTitle())
                .content(createFeedbackVo.getContent())
                .build();

        return Optional.of(feedbackRepository.save(feedbackEntity).toDomain());
    }
}
