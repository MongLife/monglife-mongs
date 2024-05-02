package com.mongs.play.app.player.internal.member.controller;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/memberX")
@RequiredArgsConstructor
@RestController
public class PlayerInternalMemberController {

    private final PlayerInternalMemberService playerInternalMemberService;


}
