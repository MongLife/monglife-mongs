package com.monglife.mongs.domain.member.listener;


import com.monglife.mongs.domain.member.dto.event.MemberObserveEvent;
import com.monglife.mongs.domain.member.entity.MemberEntity;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEntityListener {

    private final ApplicationEventPublisher applicationEventPublisher;

    @PostUpdate
    public void postUpdate(MemberEntity memberEntity) {

        MemberObserveEvent memberObserveEvent = MemberObserveEvent.builder()
                .accountId(memberEntity.getAccountId())
                .slotCount(memberEntity.getSlotCount())
                .starPoint(memberEntity.getStarPoint())
                .isActive(memberEntity.getIsActive())
                .build();

        applicationEventPublisher.publishEvent(memberObserveEvent);

    }
}
