package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.entity.MongStateEntity;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStateEvent;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongStateEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MongStateEntity mongStateEntity) {

        MongObserveStateEvent mongObserveStateEvent = MongObserveStateEvent.builder()
                .mongId(mongStateEntity.getMong().getMongId())
                .stateCode(mongStateEntity.getCode())
                .isSleep(mongStateEntity.getIsSleep())
                .build();

        applicationEventPublisher.publishEvent(mongObserveStateEvent);

    }
}
