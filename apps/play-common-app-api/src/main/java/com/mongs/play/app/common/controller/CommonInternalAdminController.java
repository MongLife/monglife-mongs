package com.mongs.play.app.common.controller;

import com.mongs.play.app.common.dto.req.*;
import com.mongs.play.app.common.dto.res.*;
import com.mongs.play.app.common.service.CommonInternalService;
import com.mongs.play.app.common.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/common/admin")
@RequiredArgsConstructor
@RestController
public class CommonInternalAdminController {

    private final CommonInternalService commonInternalService;

    @PostMapping("/version")
    public ResponseEntity<Object> registerCodeVersion(@RequestBody RegisterCodeVersionReqDto registerCodeVersionReqDto) {

        RegisterCodeVersionVo registerCodeVersionVo = commonInternalService.registerCodeVersion(registerCodeVersionReqDto.buildVersion());

        return ResponseEntity.ok().body(RegisterCodeVersionResDto.builder()
                .buildVersion(registerCodeVersionVo.buildVersion())
                .build());
    }

    /**
     * 새로운 맵 코드를 등록한다.
     *
     * @param registerMapCodeReqDto 새로운 맵 정보
     * @return null
     */
    @PostMapping("/code/map")
    public ResponseEntity<RegisterMapCodeResDto> registerMapCode(@RequestBody RegisterMapCodeReqDto registerMapCodeReqDto) {

        RegisterMapCodeVo registerMapCodeVo = commonInternalService.registerMapCode(registerMapCodeReqDto.buildVersion(), RegisterMapCodeVo.builder()
                .code(registerMapCodeReqDto.code())
                .name(registerMapCodeReqDto.name())
                .build());

        return ResponseEntity.ok().body(RegisterMapCodeResDto.builder()
                .code(registerMapCodeVo.code())
                .name(registerMapCodeVo.name())
                .build());
    }

    /**
     * 새로운 몽 코드를 등록한다.
     *
     * @param registerMongCodeReqDto 새로운 몽 정보
     * @return null
     */
    @PostMapping("/code/mong")
    public ResponseEntity<RegisterMongCodeResDto> registerMongCode(@RequestBody RegisterMongCodeReqDto registerMongCodeReqDto) {

        RegisterMongCodeVo registerMongCodeVo = commonInternalService.registerMongCode(registerMongCodeReqDto.buildVersion(), RegisterMongCodeVo.builder()
                .code(registerMongCodeReqDto.code())
                .name(registerMongCodeReqDto.name())
                .level(registerMongCodeReqDto.level())
                .evolutionPoint(registerMongCodeReqDto.evolutionPoint())
                .build());

        return ResponseEntity.ok().body(RegisterMongCodeResDto.builder()
                .code(registerMongCodeVo.code())
                .name(registerMongCodeVo.name())
                .level(registerMongCodeVo.level())
                .evolutionPoint(registerMongCodeVo.evolutionPoint())
                .build());
    }

    /**
     * 새로운 음식 코드를 등록한다.
     *
     * @param registerFoodCodeReqDto 새로운 음식 정보
     * @return null
     */
    @PostMapping("/code/food")
    public ResponseEntity<RegisterFoodCodeResDto> registerFoodCode(@RequestBody RegisterFoodCodeReqDto registerFoodCodeReqDto) {

        RegisterFoodCodeVo registerFoodCodeVo = commonInternalService.registerFoodCode(registerFoodCodeReqDto.buildVersion(), RegisterFoodCodeVo.builder()
                .code(registerFoodCodeReqDto.code())
                .name(registerFoodCodeReqDto.name())
                .groupCode(registerFoodCodeReqDto.groupCode())
                .price(registerFoodCodeReqDto.price())
                .addWeightValue(registerFoodCodeReqDto.addWeightValue())
                .addStrengthValue(registerFoodCodeReqDto.addStrengthValue())
                .addSatietyValue(registerFoodCodeReqDto.addSatietyValue())
                .addHealthyValue(registerFoodCodeReqDto.addHealthyValue())
                .addSleepValue(registerFoodCodeReqDto.addSleepValue())
                .delaySeconds(registerFoodCodeReqDto.delaySeconds())
                .build());

        return ResponseEntity.ok().body(RegisterFoodCodeResDto.builder()
                .code(registerFoodCodeVo.code())
                .name(registerFoodCodeVo.name())
                .groupCode(registerFoodCodeVo.groupCode())
                .price(registerFoodCodeVo.price())
                .addWeightValue(registerFoodCodeVo.addWeightValue())
                .addStrengthValue(registerFoodCodeVo.addStrengthValue())
                .addSatietyValue(registerFoodCodeVo.addSatietyValue())
                .addHealthyValue(registerFoodCodeVo.addHealthyValue())
                .addSleepValue(registerFoodCodeVo.addSleepValue())
                .delaySeconds(registerFoodCodeVo.delaySeconds())
                .build());
    }

    /**
     * 앱 빌드 버전을 1.0.0 으로 초기화 한다.
     *
     * @return null
     */
    @DeleteMapping("/code/reset")
    public ResponseEntity<Object> resetCode() {
        commonInternalService.resetCode();
        return ResponseEntity.ok().body(null);
    }
}
