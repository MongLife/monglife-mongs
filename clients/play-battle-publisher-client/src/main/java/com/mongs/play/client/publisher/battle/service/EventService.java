package com.mongs.play.client.publisher.battle.service;

import com.mongs.play.client.publisher.battle.event.MatchWaitEvent;
import com.mongs.play.client.publisher.battle.vo.MatchWaitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final ApplicationEventPublisher publisher;

    public void matchWaitEventPublish(MatchWaitVo matchWaitVo) {
        publisher.publishEvent(MatchWaitEvent.builder()
                .mongId(matchWaitVo.mongId())
                .build());
    }
}
