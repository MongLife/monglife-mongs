package com.monglife.mongs.app.user.player.listener;

import com.monglife.mongs.app.user.player.dto.response.MemberObserveResponseDto;
import com.monglife.mongs.app.user.player.publisher.PlayerPublisher;
import com.monglife.mongs.domain.member.dto.event.MemberObserveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberObserveListener {

    private final PlayerPublisher playerPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void memberObserveEventListener(MemberObserveEvent event) {

        playerPublisher.memberObservePublish(event.getAccountId(), MemberObserveResponseDto.builder()
                .accountId(event.getAccountId())
                .slotCount(event.getSlotCount())
                .starPoint(event.getStarPoint())
                .build());
    }
}
