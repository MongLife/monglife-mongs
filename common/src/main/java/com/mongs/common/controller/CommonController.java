package com.mongs.common.controller;

import com.mongs.common.controller.dto.request.RegisterFeedbackCodeReqDto;
import com.mongs.common.controller.dto.request.RegisterFoodCodeReqDto;
import com.mongs.common.controller.dto.request.RegisterMapCodeReqDto;
import com.mongs.common.controller.dto.request.RegisterMongCodeReqDto;
import com.mongs.common.controller.dto.response.FindCodeResDto;
import com.mongs.common.controller.dto.response.FindVersionResDto;
import com.mongs.common.service.CommonService;
import com.mongs.common.service.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final CommonService commonService;

    /**
     * 앱 빌드 버전에 따른 업데이트 내용을 확인한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @param codeIntegrity 앱 내 코드 리스트 해싱 값
     * @return {@link FindVersionResDto}
     */
    @GetMapping("/version")
    public ResponseEntity<FindVersionResDto> findNewestVersion(
            @RequestParam(value = "buildVersion", defaultValue = "1.0.0") String buildVersion,
            @RequestParam(value = "codeIntegrity", defaultValue = "") String codeIntegrity
    ) {
        FindVersionVo findVersionVo = commonService.findVersion(buildVersion, codeIntegrity);

        return ResponseEntity.ok().body(FindVersionResDto.builder()
                .newestBuildVersion(findVersionVo.newestBuildVersion())
                .createdAt(findVersionVo.createdAt())
                .mustUpdateApp(findVersionVo.mustUpdateApp())
                .mustUpdateCode(findVersionVo.mustUpdateCode())
                .build());
    }

    /**
     * 앱 빌드 버전에 해당하는 맵, 몽, 음식, 피드백에 대한 코드 목록을 조회한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return {@link FindCodeResDto}
     */
    @GetMapping("/code")
    public ResponseEntity<FindCodeResDto> findCode(
            @RequestParam(value = "buildVersion", defaultValue = "1.0.0") String buildVersion
    ) {
        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .mapCodeList(commonService.findMapCode(buildVersion))
                        .mongCodeList(commonService.findMongCode(buildVersion))
                        .foodCodeList(commonService.findFoodCode(buildVersion))
                        .feedbackCodeList(commonService.findFeedbackCode(buildVersion))
                        .build());
    }

//    @DeleteMapping("/admin/initialize")
//    public ResponseEntity<Object> initializeCode() {
//        commonService.initializeCode();
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping("/admin/code/food")
//    public ResponseEntity<Object> registerFoodCode(@RequestBody RegisterFoodCodeReqDto registerFoodCodeReqDto) {
//        commonService.registerFoodCode(RegisterFoodCodeVo.builder()
//                .code(registerFoodCodeReqDto.code())
//                .name(registerFoodCodeReqDto.name())
//                .groupCode(registerFoodCodeReqDto.groupCode())
//                .price(registerFoodCodeReqDto.price())
//                .addWeightValue(registerFoodCodeReqDto.addWeightValue())
//                .addStrengthValue(registerFoodCodeReqDto.addStrengthValue())
//                .addSatietyValue(registerFoodCodeReqDto.addSatietyValue())
//                .addHealthyValue(registerFoodCodeReqDto.addHealthyValue())
//                .addSleepValue(registerFoodCodeReqDto.addSleepValue())
//                .build(), registerFoodCodeReqDto.buildVersion());
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping("/admin/code/map")
//    public ResponseEntity<Object> registerMapCode(@RequestBody RegisterMapCodeReqDto registerMapCodeReqDto) {
//        commonService.registerMapCode(RegisterMapCodeVo.builder()
//                .code(registerMapCodeReqDto.code())
//                .name(registerMapCodeReqDto.name())
//                .build(), registerMapCodeReqDto.buildVersion());
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping("/admin/code/mong")
//    public ResponseEntity<Object> registerMongCode(@RequestBody RegisterMongCodeReqDto registerMongCodeReqDto) {
//        commonService.registerMongCode(RegisterMongCodeVo.builder()
//                .code(registerMongCodeReqDto.code())
//                .name(registerMongCodeReqDto.name())
//                .build(), registerMongCodeReqDto.buildVersion());
//        return ResponseEntity.ok().body(null);
//    }
//
//    @PostMapping("/admin/code/feedback")
//    public ResponseEntity<Object> registerFeedbackCode(@RequestBody RegisterFeedbackCodeReqDto registerFeedbackCodeReqDto) {
//        commonService.registerFeedbackCode(RegisterFeedbackCodeVo.builder()
//                .code(registerFeedbackCodeReqDto.code())
//                .groupCode(registerFeedbackCodeReqDto.groupCode())
//                .message(registerFeedbackCodeReqDto.message())
//                .build(), registerFeedbackCodeReqDto.buildVersion());
//        return ResponseEntity.ok().body(null);
//    }
}
