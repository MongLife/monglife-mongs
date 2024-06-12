package com.mongs.play.app.player.internal.member.controller;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/internal/player/member")
@RequiredArgsConstructor
@RestController
public class PlayerInternalMemberController {

    private final PlayerInternalMemberService playerInternalMemberService;
}
