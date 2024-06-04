package com.mongs.play.module.feign.client;

import com.mongs.play.config.FeignClientConfig;
import com.mongs.play.config.FeignErrorDecoder;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PLAYER-INTERNAL", configuration = FeignClientConfig.class)
public interface PlayerInternalClient {
    @PostMapping("/internal/player/collection/mapCollection")
    ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(RegisterMapCollectionReqDto registerMapCollectionReqDto);
    @PostMapping("/internal/player/collection/mongCollection")
    ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(RegisterMongCollectionReqDto registerMongCollectionReqDto);
    @DeleteMapping("/internal/player/member/increaseStarPoint/removeMapCollection")
    ResponseEntity<RemoveMapCollectionResDto> removeMapCollection(@RequestBody @Validated RemoveMapCollectionReqDto removeMapCollectionReqDto);
    @DeleteMapping("/internal/player/member/increaseStarPoint/removeMongCollection")
    ResponseEntity<RemoveMongCollectionResDto> removeMongCollection(@RequestBody @Validated RemoveMongCollectionReqDto removeMongCollectionReqDto);
    @PutMapping("/internal/player/member/increaseStarPoint/registerMapCollection")
    ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMapCollection(IncreaseStarPointReqDto increaseStarPointReqDto);
    @PutMapping("/internal/player/member/increaseStarPoint/registerMongCollection")
    ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMongCollection(IncreaseStarPointReqDto increaseStarPointReqDto);
}
