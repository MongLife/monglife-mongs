package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongEntityListener {

    private static final Double SICK_RATIO = 10D;
    private static final Double HUNGRY_RATIO = 10D;
    private static final Double SOMNOLENCE_RATIO = 10D;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void preUpdate(MongEntity mongEntity) {

        // 지수 조건 확인
        if (mongEntity.getStatus().getHealthyRatio() <= SICK_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.SICK);
        } else if (mongEntity.getStatus().getSatiety() <= HUNGRY_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.HUNGRY);
        } else if (mongEntity.getStatus().getFatigue() <= SOMNOLENCE_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.SOMNOLENCE);
        } else {
            mongEntity.getStatus().setCode(MongStatusCode.NORMAL);
        }
    }

    /**
     * 몽 변경 트리거
     * @param mongEntity 변경된 몽 엔티티
     */
    @PostUpdate
    public void postUpdate(MongEntity mongEntity) {

        applicationEventPublisher.publishEvent(MongObserveEvent.of(mongEntity));

        if (mongEntity.getStatus().getExpRatio() < 100) return;

        if (mongEntity.isEgg()) return;

        if (MongStateCode.EVOLUTION_READY.equals(mongEntity.getState().getCode())) return;

        applicationEventPublisher.publishEvent(MongEvolutionEvent.builder()
                .mongId(mongEntity.getMongId())
                .build());
    }
}
