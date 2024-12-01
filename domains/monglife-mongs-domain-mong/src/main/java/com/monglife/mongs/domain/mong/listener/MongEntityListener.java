package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MongEntity mongEntity) {

        MongObserveEvent mongObserveEvent = MongObserveEvent.builder()
                .mongId(mongEntity.getMongId())
                .payPoint(mongEntity.getPayPoint())
                .build();

        applicationEventPublisher.publishEvent(mongObserveEvent);
    }
}
