package com.mongs.collection.controller;

import com.mongs.collection.dto.request.RegisterMapCollectionReqDto;
import com.mongs.collection.dto.request.RegisterMongCollectionReqDto;
import com.mongs.collection.dto.request.RemoveMapCollectionReqDto;
import com.mongs.collection.dto.request.RemoveMongCollectionReqDto;
import com.mongs.collection.service.CollectionService;
import com.mongs.core.security.principal.PassportDetail;
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

    @PostMapping("/map")
    public ResponseEntity<Object> registerMapCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMapCollectionReqDto registerMapCollectionReqDto
    ) {
        return ResponseEntity.ok().body(
                collectionService.registerMapCollection(
                        passportDetail.getId(),
                        registerMapCollectionReqDto.mapCode()
                ));
    }

    @PostMapping("/mong")
    public ResponseEntity<Object> registerMongCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RegisterMongCollectionReqDto registerMongCollectionReqDto
    ) {
        return ResponseEntity.ok().body(
                collectionService.registerMongCollection(
                        passportDetail.getId(),
                        registerMongCollectionReqDto.mongCode()
                ));
    }

    @DeleteMapping("/map")
    public ResponseEntity<Object> removeMapCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RemoveMapCollectionReqDto removeMapCollectionReqDto
    ) {
        collectionService.removeMapCollection(passportDetail.getId(), removeMapCollectionReqDto.mapCode());
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/mong")
    public ResponseEntity<Object> removeMongCollection(
            @AuthenticationPrincipal PassportDetail passportDetail,
            @RequestBody @Validated RemoveMongCollectionReqDto removeMongCollectionReqDto
    ) {
        collectionService.removeMapCollection(passportDetail.getId(), removeMongCollectionReqDto.mongCode());
        return ResponseEntity.ok().body(null);
    }
}
