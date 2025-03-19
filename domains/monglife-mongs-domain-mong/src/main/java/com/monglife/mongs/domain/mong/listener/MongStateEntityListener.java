package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongStateObserveEvent;
import com.monglife.mongs.domain.mong.entity.MongStateEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStateEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MongStateEntity mongStateEntity) {

        applicationEventPublisher.publishEvent(MongStateObserveEvent.builder()
                .mongId(mongStateEntity.getMong().getMongId())
                .stateCode(mongStateEntity.getCode())
                .isSleep(mongStateEntity.getIsSleep())
                .updatedAt(mongStateEntity.getUpdatedAt())
                .build());
    }
}
