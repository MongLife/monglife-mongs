package com.mongs.play.app.player.internal.collection.controller;

import com.mongs.play.app.player.internal.collection.service.PlayerInternalCollectionService;
import com.mongs.play.module.feign.dto.req.RegisterMapCollectionReqDto;
import com.mongs.play.module.feign.dto.req.RegisterMongCollectionReqDto;
import com.mongs.play.module.feign.dto.res.RegisterMapCollectionResDto;
import com.mongs.play.module.feign.dto.res.RegisterMongCollectionResDto;
import com.mongs.play.module.feign.service.PlayerInternalMemberFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/internal/player/collection")
@RequiredArgsConstructor
@RestController
public class PlayerInternalCollectionController {

    private final PlayerInternalCollectionService playerInternalCollectionService;
    private final PlayerInternalMemberFeignService playerInternalMemberFeignService;

    @PostMapping("/mapCollection")
    public ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(@RequestBody RegisterMapCollectionReqDto registerMapCollectionReqDto) {
        Long accountId = registerMapCollectionReqDto.accountId();
        String mapCode = registerMapCollectionReqDto.mapCode();

        log.info("[registerMapCollection] accountId: {}, mapCode: {}", accountId, mapCode);

        var vo = playerInternalCollectionService.registerMapCollection(accountId, mapCode);

        var res = playerInternalMemberFeignService.increaseStarPointByRegisterMapCollection(accountId);

        return ResponseEntity.ok(RegisterMapCollectionResDto.builder()
                .accountId(vo.accountId())
                .starPoint(res.starPoint())
                .build());
    }

    @PostMapping("/mongCollection")
    public ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(@RequestBody RegisterMongCollectionReqDto registerMongCollectionReqDto) {
        Long accountId = registerMongCollectionReqDto.accountId();
        String mongCode = registerMongCollectionReqDto.mongCode();

        log.info("[registerMongCollection] accountId: {}, mongCode: {}", accountId, mongCode);

        var vo = playerInternalCollectionService.registerMongCollection(accountId, mongCode);

        var res = playerInternalMemberFeignService.increaseStarPointByRegisterMongCollection(accountId);

        return ResponseEntity.ok(RegisterMongCollectionResDto.builder()
                .accountId(vo.accountId())
                .starPoint(res.starPoint())
                .build());
    }

}