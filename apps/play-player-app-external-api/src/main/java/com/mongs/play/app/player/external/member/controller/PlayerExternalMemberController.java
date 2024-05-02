package com.mongs.play.app.player.external.member.controller;

import com.mongs.play.app.player.external.member.service.PlayerExternalMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class PlayerExternalMemberController {

    private final PlayerExternalMemberService playerExternalMemberService;


}
