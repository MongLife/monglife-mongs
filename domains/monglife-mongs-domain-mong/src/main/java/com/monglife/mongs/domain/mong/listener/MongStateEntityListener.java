package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongObserveStateEvent;
import com.monglife.mongs.domain.mong.entity.MongStateEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStateEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 몽 상태 변경 트리거
     * @param mongStateEntity 변경된 몽 상태 엔티티
     */
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
