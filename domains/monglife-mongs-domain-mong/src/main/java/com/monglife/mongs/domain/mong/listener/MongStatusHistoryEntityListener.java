package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongStatusHistoryEvent;
import com.monglife.mongs.domain.mong.entity.MongStatusHistoryEntity;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStatusHistoryEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostPersist
    public void postPersist(MongStatusHistoryEntity mongStatusHistoryEntity) {

        applicationEventPublisher.publishEvent(MongStatusHistoryEvent.builder()
                .type(mongStatusHistoryEntity.getMongStatusHistoryType())
                .code(mongStatusHistoryEntity.getCode())
                .accountId(mongStatusHistoryEntity.getAccountId())
                .mongName(mongStatusHistoryEntity.getMongName())
                .build());
    }
}
