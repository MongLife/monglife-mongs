package com.mongs.play.app.player.external.collection.controller;

import com.mongs.play.app.player.external.collection.dto.res.FindMapCollectionResDto;
import com.mongs.play.app.player.external.collection.dto.res.FindMongCollectionResDto;
import com.mongs.play.app.player.external.collection.service.PlayerExternalCollectionService;
import com.mongs.play.app.player.external.collection.vo.FindMapCollectionVo;
import com.mongs.play.app.player.external.collection.vo.FindMongCollectionVo;
import com.mongs.play.module.security.principal.PassportDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/collection")
@RequiredArgsConstructor
@RestController
public class PlayerExternalCollectionController {

    private final PlayerExternalCollectionService playerExternalCollectionService;

    @GetMapping("/map")
    public ResponseEntity<List<FindMapCollectionResDto>> findMapCollection(@AuthenticationPrincipal PassportDetail passportDetail) {

        List<FindMapCollectionVo> findMapCollectionVo = playerExternalCollectionService.findMapCollection(passportDetail.getId());

        return ResponseEntity.ok().body(FindMapCollectionResDto.toList(findMapCollectionVo));
    }

    @GetMapping("/mong")
    public ResponseEntity<List<FindMongCollectionResDto>> findMongCollection(@AuthenticationPrincipal PassportDetail passportDetail) {

        List<FindMongCollectionVo> findMongCollectionVoList = playerExternalCollectionService.findMongCollection(passportDetail.getId());

        return ResponseEntity.ok().body(FindMongCollectionResDto.toList(findMongCollectionVoList));
    }
}
