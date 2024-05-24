package com.mongs.play.app.management.internal.controller;

import com.mongs.play.app.management.internal.service.ManagementInternalService;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
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
//
//    @PutMapping("/evolutionReady")
//    public ResponseEntity<EvolutionReadyResDto> evolutionReady(@RequestBody EvolutionReadyReqDto evolutionReadyReqDto) {
//        Long mongId = evolutionReadyReqDto.mongId();
//
//        var vo = managementInternalService.evolutionReady(mongId);
//
//        return ResponseEntity.ok().body(EvolutionReadyResDto.builder()
//                .mongId(vo.mongId())
//                .shiftCode(vo.shiftCode())
//                .build());
//    }
//
//    @PutMapping("/decreaseStatus")
//    public ResponseEntity<DecreaseStatusResDto> decreaseStatus(@RequestBody DecreaseStatusReqDto decreaseStatusReqDto) {
//        Long mongId = decreaseStatusReqDto.mongId();
//        Double subWeight = decreaseStatusReqDto.subWeight();
//        Double subStrength = decreaseStatusReqDto.subStrength();
//        Double subSatiety = decreaseStatusReqDto.subSatiety();
//        Double subHealthy = decreaseStatusReqDto.subHealthy();
//        Double subSleep = decreaseStatusReqDto.subSleep();
//
//        var vo = managementInternalService.decreaseStatus(mongId, subWeight, subStrength, subSatiety, subHealthy, subSleep);
//
//        return ResponseEntity.ok().body(DecreaseStatusResDto.builder()
//                .mongId(vo.mongId())
//                .weight(vo.weight())
//                .strength(vo.strength())
//                .satiety(vo.satiety())
//                .healthy(vo.healthy())
//                .sleep(vo.sleep())
//                .build());
//    }
//
//    @PutMapping("/increaseStatus")
//    public ResponseEntity<IncreaseStatusResDto> increaseStatus(@RequestBody IncreaseStatusReqDto increaseStatusReqDto) {
//        Long mongId = increaseStatusReqDto.mongId();
//        Double addWeight = increaseStatusReqDto.addWeight();
//        Double addStrength = increaseStatusReqDto.addStrength();
//        Double addSatiety = increaseStatusReqDto.addSatiety();
//        Double addHealthy = increaseStatusReqDto.addHealthy();
//        Double addSleep = increaseStatusReqDto.addSleep();
//
//        var vo = managementInternalService.increaseStatus(mongId, addWeight, addStrength, addSatiety, addHealthy, addSleep);
//
//        return ResponseEntity.ok().body(IncreaseStatusResDto.builder()
//                .mongId(vo.mongId())
//                .weight(vo.weight())
//                .strength(vo.strength())
//                .satiety(vo.satiety())
//                .healthy(vo.healthy())
//                .sleep(vo.sleep())
//                .build());
//    }
//
//    @PutMapping("/increasePoopCount")
//    public ResponseEntity<IncreasePoopCountResDto> increasePoopCount(@RequestBody IncreasePoopCountReqDto increasePoopCountReqDto) {
//        Long mongId = increasePoopCountReqDto.mongId();
//        Integer addPoopCount = increasePoopCountReqDto.addPoopCount();
//
//        var vo = managementInternalService.increasePoopCount(mongId, addPoopCount);
//
//        return ResponseEntity.ok().body(IncreasePoopCountResDto.builder()
//                .mongId(vo.mongId())
//                .poopCount(vo.poopCount())
//                .build());
//    }
//
//    @PutMapping("/dead")
//    public ResponseEntity<DeadResDto> dead(@RequestBody DeadReqDto deadReqDto) {
//        Long mongId = deadReqDto.mongId();
//
//        var vo = managementInternalService.dead(mongId);
//
//        return ResponseEntity.ok().body(DeadResDto.builder()
//                .mongId(vo.mongId())
//                .build());
//    }

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
