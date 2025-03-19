package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongStateHistoryEvent;
import com.monglife.mongs.domain.mong.entity.MongStateHistoryEntity;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStateHistoryEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostPersist
    public void postPersist(MongStateHistoryEntity mongStateHistoryEntity) {

        applicationEventPublisher.publishEvent(MongStateHistoryEvent.builder()
                .type(mongStateHistoryEntity.getMongStateHistoryType())
                .code(mongStateHistoryEntity.getCode())
                .accountId(mongStateHistoryEntity.getAccountId())
                .mongName(mongStateHistoryEntity.getMongName())
                .build());
    }
}
