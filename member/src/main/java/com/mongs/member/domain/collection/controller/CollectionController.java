package com.mongs.member.domain.collection.controller;

import com.mongs.core.security.principal.PassportDetail;
import com.mongs.member.domain.collection.controller.dto.request.RegisterMapCollectionReqDto;
import com.mongs.member.domain.collection.controller.dto.request.RegisterMongCollectionReqDto;
import com.mongs.member.domain.collection.controller.dto.request.RemoveMapCollectionReqDto;
import com.mongs.member.domain.collection.controller.dto.request.RemoveMongCollectionReqDto;
import com.mongs.member.domain.collection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collection")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/map")
    public ResponseEntity<Object> findMapCollection(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity.ok().body(collectionService.findMapCollection(passportDetail.getId()));
    }

    @GetMapping("/mong")
    public ResponseEntity<Object> findMongCollection(@AuthenticationPrincipal PassportDetail passportDetail) {
        return ResponseEntity.ok().body(collectionService.findMongCollection(passportDetail.getId()));
    }
    @PostMapping("/admin/map/{accountId}")
    public ResponseEntity<Object> registerMapCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RegisterMapCollectionReqDto registerMapCollectionReqDto
    ) {
        return ResponseEntity.ok().body(collectionService.registerMapCollection(accountId, registerMapCollectionReqDto.mapCode()));
    }
    @PostMapping("/admin/mong/{accountId}")
    public ResponseEntity<Object> registerMongCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RegisterMongCollectionReqDto registerMongCollectionReqDto
    ) {
        return ResponseEntity.ok().body(collectionService.registerMongCollection(accountId, registerMongCollectionReqDto.mongCode()));
    }

    @DeleteMapping("/admin/map/{accountId}")
    public ResponseEntity<Object> removeMapCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMapCollectionReqDto removeMapCollectionReqDto
    ) {
        return ResponseEntity.ok().body(collectionService.removeMapCollection(accountId, removeMapCollectionReqDto.mapCode()));
    }
    @DeleteMapping("/admin/mong/{accountId}")
    public ResponseEntity<Object> removeMongCollection(
            @PathVariable("accountId") Long accountId,
            @RequestBody @Validated RemoveMongCollectionReqDto removeMongCollectionReqDto
    ) {
        return ResponseEntity.ok().body(collectionService.removeMongCollection(accountId, removeMongCollectionReqDto.mongCode()));
    }
}
