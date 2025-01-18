package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.dto.event.MongObserveEvent;
import com.monglife.mongs.domain.mong.entity.MongEntity;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongEntityListener {

    // 아픔 상태 돌입 체력 지수 비율 (퍼센트)
    private static final Double HEALTH_SICK_RATIO = 10D;

    // 배고픔 상태 돌입 포만감 지수 비율
    private static final Double SATIETY_HUNGRY_RATIO = 10D;

    // 피곤함 상태 돌입 피로도 지수 비율
    private static final Double FATIGUE_SOMNOLENCE_RATIO = 10D;

    private final ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void preUpdate(MongEntity mongEntity) {

        // 지수 코드 변경 조건 확인
        if (mongEntity.getStatus().getHealthyRatio() <= HEALTH_SICK_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.SICK);
        } else if (mongEntity.getStatus().getSatiety() <= SATIETY_HUNGRY_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.HUNGRY);
        } else if (mongEntity.getStatus().getFatigue() <= FATIGUE_SOMNOLENCE_RATIO) {
            mongEntity.getStatus().setCode(MongStatusCode.SOMNOLENCE);
        } else {
            mongEntity.getStatus().setCode(MongStatusCode.NORMAL);
        }
    }

    /**
     * 몽 엔티티 변경 리스너
     * @param mongEntity 변경된 몽 엔티티
     */
    @PostUpdate
    public void postUpdate(MongEntity mongEntity) {

        applicationEventPublisher.publishEvent(MongObserveEvent.of(mongEntity));

        if (mongEntity.getStatus().getExpRatio() < 100) return;

        if (mongEntity.isEgg()) return;

        if (mongEntity.isGraduateReady()) return;

        if (mongEntity.isDead()) return;

        if (!mongEntity.isEvolutionReady()) {
            applicationEventPublisher.publishEvent(MongEvolutionEvent.builder()
                    .mongId(mongEntity.getMongId())
                    .build());
        }
    }
}
