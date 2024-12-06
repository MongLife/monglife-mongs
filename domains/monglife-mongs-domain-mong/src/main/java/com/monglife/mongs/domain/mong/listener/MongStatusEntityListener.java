package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.entity.MongStatusEntity;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongStatusEntityListener {

    private static final Double SICK_RATIO = 10D;
    private static final Double HUNGRY_RATIO = 10D;
    private static final Double SOMNOLENCE_RATIO = 10D;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void preUpdate(MongStatusEntity mongStatusEntity) {

        if (mongStatusEntity.getHealthyRatio() <= SICK_RATIO) {
            mongStatusEntity.update(MongStatusEntity.UpdateDto.builder()
                    .code(MongStatusCode.SICK)
                    .build());
        } else if (mongStatusEntity.getSatiety() <= HUNGRY_RATIO) {
            mongStatusEntity.update(MongStatusEntity.UpdateDto.builder()
                    .code(MongStatusCode.HUNGRY)
                    .build());
        } else if (mongStatusEntity.getFatigue() <= SOMNOLENCE_RATIO) {
            mongStatusEntity.update(MongStatusEntity.UpdateDto.builder()
                    .code(MongStatusCode.SOMNOLENCE)
                    .build());
        } else {
            mongStatusEntity.update(MongStatusEntity.UpdateDto.builder()
                    .code(MongStatusCode.NORMAL)
                    .build());
        }

        if (mongStatusEntity.getExpRatio() >= 100) mongStatusEntity.getMong().evolutionReady();
    }

    /**
     * 몽 지수 변경 트리거
     * @param mongStatusEntity 변경된 몽 지수 엔티티
     */
    @PostUpdate
    public void postUpdate(MongStatusEntity mongStatusEntity) {

        MongObserveStatusEvent mongObserveStatusEvent = MongObserveStatusEvent.builder()
                .mongId(mongStatusEntity.getMong().getMongId())
                .statusCode(mongStatusEntity.getCode())
                .weight(mongStatusEntity.getWeight())
                .expRatio(mongStatusEntity.getExpRatio())
                .strengthRatio(mongStatusEntity.getStrengthRatio())
                .satietyRatio(mongStatusEntity.getSatietyRatio())
                .healthyRatio(mongStatusEntity.getHealthyRatio())
                .fatigueRatio(mongStatusEntity.getFatigueRatio())
                .poopCount(mongStatusEntity.getPoopCount())
                .stateCode(mongStatusEntity.getMong().getState().getCode())
                .build();

        applicationEventPublisher.publishEvent(mongObserveStatusEvent);
    }
}
