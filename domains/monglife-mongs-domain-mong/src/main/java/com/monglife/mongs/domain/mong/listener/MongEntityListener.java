package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.entity.data.MongEntity;
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
public class MongEntityListener {

    private static final Double SICK_RATIO = 10D;
    private static final Double HUNGRY_RATIO = 10D;
    private static final Double SOMNOLENCE_RATIO = 10D;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void preUpdate(MongEntity mongEntity) {

        // 지수 최대 최소 값 validation 처리
        mongEntity.getStatus().sync();

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

        MongObserveEvent mongObserveEvent = MongObserveEvent.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .payPoint(mongEntity.getPayPoint())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .stateCode(mongEntity.getState().getCode())
                .isSleep(mongEntity.getState().getIsSleep())
                .statusCode(mongEntity.getStatus().getCode())
                .weight(mongEntity.getStatus().getWeight())
                .expRatio(mongEntity.getStatus().getExpRatio())
                .strengthRatio(mongEntity.getStatus().getStrengthRatio())
                .satietyRatio(mongEntity.getStatus().getSatietyRatio())
                .healthyRatio(mongEntity.getStatus().getHealthyRatio())
                .fatigueRatio(mongEntity.getStatus().getFatigueRatio())
                .poopCount(mongEntity.getStatus().getPoopCount())
                .createdAt(mongEntity.getCreatedAt())
                .updatedAt(mongEntity.getUpdatedAt())
                .build();

        applicationEventPublisher.publishEvent(mongObserveEvent);

        // 진화 조건 확인
        if (mongEntity.getStatus().getExpRatio() >= 100) {
            applicationEventPublisher.publishEvent(MongEvolutionEvent.builder()
                    .mongId(mongEntity.getMongId())
                    .build());
        }
    }
}
