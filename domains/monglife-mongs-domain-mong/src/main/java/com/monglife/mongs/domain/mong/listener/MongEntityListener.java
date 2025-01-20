package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongBasicObserveEvent;
import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.entity.MongEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 몽 엔티티 변경 리스너
     * @param mongEntity 변경된 몽 엔티티
     */
    @PostUpdate
    public void postUpdate(MongEntity mongEntity) {

        applicationEventPublisher.publishEvent(MongBasicObserveEvent.builder()
                .mongId(mongEntity.getMongId())
                .mongName(mongEntity.getMongName())
                .mongTypeCode(mongEntity.getType().getComn().getCode())
                .payPoint(mongEntity.getPayPoint())
                .createdAt(mongEntity.getCreatedAt())
                .updatedAt(mongEntity.getUpdatedAt())
                .build());

        if (mongEntity.getStatus().getExpRatio() < 100) return;

        if (mongEntity.isEgg()) return;

        if (mongEntity.isGraduateReady()) return;

        if (mongEntity.isDead()) return;

        if (!mongEntity.isEvolutionReady()) {
            // 진화 준비 상태가 된 경우 몽 진화 이벤트 발생
            applicationEventPublisher.publishEvent(MongEvolutionEvent.builder()
                    .mongId(mongEntity.getMongId())
                    .build());
        }
    }
}
