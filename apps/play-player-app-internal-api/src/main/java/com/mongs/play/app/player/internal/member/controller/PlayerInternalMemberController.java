package com.mongs.play.app.player.internal.member.controller;

import com.mongs.play.app.player.internal.member.service.PlayerInternalMemberService;
import com.mongs.play.module.feign.dto.req.IncreaseStarPointReqDto;
import com.mongs.play.module.feign.dto.res.IncreaseStarPointResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/internal/member")
@RequiredArgsConstructor
@RestController
public class PlayerInternalMemberController {

    private final PlayerInternalMemberService playerInternalMemberService;

    @PutMapping("/increaseStarPoint/registerMapCollection")
    public ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMapCollection(@RequestBody IncreaseStarPointReqDto increaseStarPointReqDto) {
        Long accountId = increaseStarPointReqDto.accountId();

        var vo = playerInternalMemberService.increaseStarPointMapCollection(accountId);

        return ResponseEntity.ok().body(IncreaseStarPointResDto.builder()
                .accountId(vo.accountId())
                .starPoint(vo.starPoint())
                .build());
    }

    @PutMapping("/increaseStarPoint/registerMongCollection")
    public ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMongCollection(@RequestBody IncreaseStarPointReqDto increaseStarPointReqDto) {
        Long accountId = increaseStarPointReqDto.accountId();

        var vo = playerInternalMemberService.increaseStarPointMongCollection(accountId);

        return ResponseEntity.ok().body(IncreaseStarPointResDto.builder()
                .accountId(vo.accountId())
                .starPoint(vo.starPoint())
                .build());
    }
}
