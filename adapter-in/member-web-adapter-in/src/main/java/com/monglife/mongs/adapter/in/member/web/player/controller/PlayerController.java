package com.monglife.mongs.adapter.in.member.web.player.controller;

import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerUseCase playerUseCase;
}
