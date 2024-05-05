package com.mongs.play.app.player.internal.member.controller;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/member")
@RequiredArgsConstructor
@RestController
public class PlayerInternalMemberController {

    private final PlayerInternalMemberService playerInternalMemberService;

}
