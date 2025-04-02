package com.mongs.play.app.management.internal.controller;

import com.mongs.play.app.management.internal.dto.req.IncreasePayPointReqDto;
import com.mongs.play.app.management.internal.dto.res.IncreasePayPointResDto;
import com.mongs.play.app.management.internal.service.ManagementInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/management")
@RequiredArgsConstructor
@RestController
public class ManagementInternalController {

    private final ManagementInternalService managementInternalService;

    @PutMapping("/increasePayPoint")
    public ResponseEntity<IncreasePayPointResDto> increasePayPoint(@RequestBody IncreasePayPointReqDto increasePayPointReqDto) {
        Long mongId = increasePayPointReqDto.mongId();
        Integer addPayPoint = increasePayPointReqDto.addPayPoint();

        var vo = managementInternalService.increasePayPoint(mongId, addPayPoint);

        return ResponseEntity.ok().body(IncreasePayPointResDto.builder()
                .mongId(vo.mongId())
                .payPoint(vo.payPoint())
                .build());
    }
}
