package com.monglife.mongs.adapter.in.member.sub.player.consumer;

import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerRollbackConsumer {

    private final PlayerUseCase playerUseCase;
}
