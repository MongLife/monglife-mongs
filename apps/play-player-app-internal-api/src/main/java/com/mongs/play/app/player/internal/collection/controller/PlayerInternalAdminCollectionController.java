package com.mongs.play.app.player.internal.collection.controller;

import com.mongs.play.app.player.internal.collection.dto.req.RegisterMapCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.req.RegisterMongCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.req.RemoveMapCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.req.RemoveMongCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.res.RegisterMapCollectionResDto;
import com.mongs.play.app.player.internal.collection.dto.res.RegisterMongCollectionResDto;
import com.mongs.play.app.player.internal.collection.dto.res.RemoveMapCollectionResDto;
import com.mongs.play.app.player.internal.collection.dto.res.RemoveMongCollectionResDto;
import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.app.player.internal.collection.vo.RegisterMapCollectionVo;
import com.mongs.play.app.player.internal.collection.vo.RegisterMongCollectionVo;
import com.mongs.play.app.player.internal.collection.vo.RemoveMapCollectionVo;
import com.mongs.play.app.player.internal.collection.vo.RemoveMongCollectionVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/collection/admin")
@RequiredArgsConstructor
@RestController
public class PlayerInternalAdminCollectionController {

    private final PlayerInternalCollectionService playerInternalCollectionService;

    @PostMapping("/map")
    public ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMapCollectionReqDto registerMapCollectionReqDto
    ) {
        RegisterMapCollectionVo registerMapCollectionVo =
                playerInternalCollectionService.registerMapCollection(passportDetail.getId(), registerMapCollectionReqDto.mapCode());

        return ResponseEntity.ok().body(RegisterMapCollectionResDto.builder()
                .accountId(registerMapCollectionVo.accountId())
                .code(registerMapCollectionVo.code())
                .createdAt(registerMapCollectionVo.createdAt())
                .build());
    }

    @PostMapping("/mong")
    public ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMongCollectionReqDto registerMongCollectionReqDto
    ) {
        RegisterMongCollectionVo registerMongCollectionVo =
                playerInternalCollectionService.registerMongCollection(passportDetail.getId(), registerMongCollectionReqDto.mongCode());

        return ResponseEntity.ok().body(RegisterMongCollectionResDto.builder()
                .accountId(registerMongCollectionVo.accountId())
                .code(registerMongCollectionVo.code())
                .createdAt(registerMongCollectionVo.createdAt())
                .build());
    }

    @DeleteMapping("/map/{accountId}")
    public ResponseEntity<RemoveMapCollectionResDto> removeMapCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMapCollectionReqDto removeMapCollectionReqDto
    ) {
        RemoveMapCollectionVo removeMapCollectionVo =
                playerInternalCollectionService.removeMapCollection(accountId, removeMapCollectionReqDto.mapCode());

        return ResponseEntity.ok().body(RemoveMapCollectionResDto.builder()
                .accountId(removeMapCollectionVo.accountId())
                .code(removeMapCollectionVo.code())
                .build());
    }

    @DeleteMapping("/mong/{accountId}")
    public ResponseEntity<Object> removeMongCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMongCollectionReqDto removeMongCollectionReqDto
    ) {
        RemoveMongCollectionVo removeMongCollectionVo =
                playerInternalCollectionService.removeMongCollection(accountId, removeMongCollectionReqDto.mongCode());

        return ResponseEntity.ok().body(RemoveMongCollectionResDto.builder()
                .accountId(removeMongCollectionVo.accountId())
                .code(removeMongCollectionVo.code())
                .build());
    }
}
