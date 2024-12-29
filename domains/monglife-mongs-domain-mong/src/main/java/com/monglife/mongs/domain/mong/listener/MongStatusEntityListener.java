package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveStatusEvent;
import com.monglife.mongs.domain.mong.entity.data.MongStatusEntity;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongStatusEntityListener {

    private static final Double SICK_RATIO = 10D;
    private static final Double HUNGRY_RATIO = 10D;
    private static final Double SOMNOLENCE_RATIO = 10D;

    private final ApplicationEventPublisher applicationEventPublisher;


    @PreUpdate
    public void preUpdate(MongStatusEntity mongStatusEntity) {

        // 지수 최대 최소 값 validation 처리
        mongStatusEntity.sync();

        // 지수 조건 확인
        if (mongStatusEntity.getHealthyRatio() <= SICK_RATIO) {
            mongStatusEntity.setCode(MongStatusCode.SICK);
        } else if (mongStatusEntity.getSatiety() <= HUNGRY_RATIO) {
            mongStatusEntity.setCode(MongStatusCode.HUNGRY);
        } else if (mongStatusEntity.getFatigue() <= SOMNOLENCE_RATIO) {
            mongStatusEntity.setCode(MongStatusCode.SOMNOLENCE);
        } else {
            mongStatusEntity.setCode(MongStatusCode.NORMAL);
        }
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

        // 진화 조건 확인
        if (mongStatusEntity.getExpRatio() >= 100) {
            applicationEventPublisher.publishEvent(MongEvolutionEvent.builder()
                    .mongId(mongStatusEntity.getMong().getMongId())
                    .build());
        }
    }
}
