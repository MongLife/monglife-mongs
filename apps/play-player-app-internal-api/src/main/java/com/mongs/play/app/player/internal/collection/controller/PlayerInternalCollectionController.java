package com.mongs.play.app.player.internal.collection.controller;

import com.mongs.play.app.player.internal.collection.dto.req.RegisterMapCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.req.RegisterMongCollectionReqDto;
import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/collection")
@RequiredArgsConstructor
@RestController
public class PlayerInternalCollectionController {

    private final PlayerInternalCollectionService playerInternalCollectionService;

    @PostMapping("/map")
    public ResponseEntity<Object> registerMapCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMapCollectionReqDto registerMapCollectionReqDto
    ) {
        return ResponseEntity.ok().body(playerInternalCollectionService.registerMapCollection(passportDetail.getId(), registerMapCollectionReqDto.mapCode()));
    }
    @PostMapping("/mong")
    public ResponseEntity<Object> registerMongCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMongCollectionReqDto registerMongCollectionReqDto
    ) {
        return ResponseEntity.ok().body(playerInternalCollectionService.registerMongCollection(passportDetail.getId(), registerMongCollectionReqDto.mongCode()));
    }
}
