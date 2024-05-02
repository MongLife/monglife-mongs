package com.mongs.play.app.common.internal.controller;

import com.mongs.play.app.common.internal.dto.req.*;
import com.mongs.play.app.common.internal.dto.res.*;
import com.mongs.play.app.common.internal.service.CommonInternalService;
import com.mongs.play.app.common.internal.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/internal/common")
@RequiredArgsConstructor
@RestController
public class CommonInternalController {

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
                .build());

        return ResponseEntity.ok().body(RegisterMongCodeResDto.builder()
                .code(registerMongCodeVo.code())
                .name(registerMongCodeVo.name())
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
                .build());
    }

    /**
     * 새로운 피드백 코드를 등록한다.
     *
     * @param registerFeedbackCodeReqDto 새로운 피드백 정보
     * @return null
     */
    @PostMapping("/code/feedback")
    public ResponseEntity<RegisterFeedbackCodeResDto> registerFeedbackCode(@RequestBody RegisterFeedbackCodeReqDto registerFeedbackCodeReqDto) {

        RegisterFeedbackCodeVo registerFeedbackCodeVo = commonInternalService.registerFeedbackCode(registerFeedbackCodeReqDto.buildVersion(), RegisterFeedbackCodeVo.builder()
                .code(registerFeedbackCodeReqDto.code())
                .groupCode(registerFeedbackCodeReqDto.groupCode())
                .message(registerFeedbackCodeReqDto.message())
                .build());

        return ResponseEntity.ok().body(RegisterFeedbackCodeResDto.builder()
                .code(registerFeedbackCodeVo.code())
                .groupCode(registerFeedbackCodeVo.groupCode())
                .message(registerFeedbackCodeVo.message())
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
