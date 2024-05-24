package com.mongs.play.module.feign.client;

import com.mongs.play.config.FeignClientConfig;
import com.mongs.play.config.FeignErrorDecoder;
import com.mongs.play.module.feign.dto.req.IncreaseStarPointReqDto;
import com.mongs.play.module.feign.dto.req.RegisterMapCollectionReqDto;
import com.mongs.play.module.feign.dto.req.RegisterMongCollectionReqDto;
import com.mongs.play.module.feign.dto.res.IncreaseStarPointResDto;
import com.mongs.play.module.feign.dto.res.RegisterMapCollectionResDto;
import com.mongs.play.module.feign.dto.res.RegisterMongCollectionResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "PLAYER-INTERNAL", configuration = FeignClientConfig.class)
public interface PlayerInternalClient {
    @PostMapping("/internal/player/collection/mapCollection")
    ResponseEntity<RegisterMapCollectionResDto> registerMapCollection(RegisterMapCollectionReqDto registerMapCollectionReqDto);
    @PostMapping("/internal/player/collection/mongCollection")
    ResponseEntity<RegisterMongCollectionResDto> registerMongCollection(RegisterMongCollectionReqDto registerMongCollectionReqDto);
    @PutMapping("/internal/player/member/increaseStarPoint/registerMapCollection")
    ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMapCollection(IncreaseStarPointReqDto increaseStarPointReqDto);
    @PutMapping("/internal/player/member/increaseStarPoint/registerMongCollection")
    ResponseEntity<IncreaseStarPointResDto> increaseStarPointByRegisterMongCollection(IncreaseStarPointReqDto increaseStarPointReqDto);
}
