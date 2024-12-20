package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.entity.data.MongEntity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;


    /**
     * 몽 변경 트리거
     * @param mongEntity 변경된 몽 엔티티
     */
    @PostUpdate
    public void postUpdate(MongEntity mongEntity) {

        MongObserveEvent mongObserveEvent = MongObserveEvent.builder()
                .mongId(mongEntity.getMongId())
                .payPoint(mongEntity.getPayPoint())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .build();

        applicationEventPublisher.publishEvent(mongObserveEvent);
    }
}
