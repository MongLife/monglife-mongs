package com.monglife.mongs.domain.match.listener;

import com.monglife.mongs.domain.match.dto.event.EnterMatchEvent;
import com.monglife.mongs.domain.match.dto.event.MatchObserveEvent;
import com.monglife.mongs.domain.match.dto.event.OverMatchEvent;
import com.monglife.mongs.domain.match.entity.MatchPlayerEntity;
import com.monglife.mongs.domain.match.entity.MatchRoomEntity;
import com.monglife.mongs.domain.match.utils.MatchUtil;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MatchRoomEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void preUpdate(MatchRoomEntity matchRoomEntity) {

        if (matchRoomEntity.isPlayerEnterAll()) {
            // 입장 완료
            matchRoomEntity.start();
        } else if (matchRoomEntity.isRoundPickAll()) {
            // 선택 완료

        } else if (matchRoomEntity.isPlayerExitAll()) {
            // 퇴장 완료
            matchRoomEntity.over();
        }
    }

    @PostUpdate
    public void postUpdate(MatchRoomEntity matchRoomEntity) {

        if (matchRoomEntity.isPlayerEnterAll()) {
            // 입장 완료
            applicationEventPublisher.publishEvent(EnterMatchEvent.of(matchRoomEntity));

        } else if (matchRoomEntity.isRoundPickAll()) {
            // 선택 완료
            applicationEventPublisher.publishEvent(MatchObserveEvent.of(matchRoomEntity));
        } else if (matchRoomEntity.isOver()) {
            // 퇴장 완료
            Set<MatchPlayerEntity> matchPlayerEntities =  matchRoomEntity.getMatchPlayerSet();

            List<MatchPlayerEntity> rankMatchPlayerEntities = MatchUtil.rankMatchPlayer(matchPlayerEntities);

            rankMatchPlayerEntities.stream().findFirst()
                    .ifPresent(matchPlayerEntity ->
                            applicationEventPublisher.publishEvent(OverMatchEvent.of(matchRoomEntity, matchPlayerEntity)));
        }
    }
}
