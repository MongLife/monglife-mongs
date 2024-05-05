package com.mongs.play.app.player.internal.collection.controller;

import com.mongs.play.app.player.internal.collection.dto.req.RemoveMapCollectionReqDto;
import com.mongs.play.app.player.internal.collection.dto.req.RemoveMongCollectionReqDto;
import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/collection/admin")
@RequiredArgsConstructor
@RestController
public class PlayerInternalAdminCollectionController {

    private final PlayerInternalCollectionService playerInternalCollectionService;

    @DeleteMapping("/map/{accountId}")
    public ResponseEntity<Object> removeMapCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMapCollectionReqDto removeMapCollectionReqDto
    ) {
        return ResponseEntity.ok().body(playerInternalCollectionService.removeMapCollection(accountId, removeMapCollectionReqDto.mapCode()));
    }

    @DeleteMapping("/mong/{accountId}")
    public ResponseEntity<Object> removeMongCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMongCollectionReqDto removeMongCollectionReqDto
    ) {
        return ResponseEntity.ok().body(playerInternalCollectionService.removeMongCollection(accountId, removeMongCollectionReqDto.mongCode()));
    }
}
