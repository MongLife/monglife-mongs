package com.mongs.common.controller;

import com.mongs.common.controller.dto.response.FindCodeResDto;
import com.mongs.common.controller.dto.response.FindVersionResDto;
import com.mongs.common.service.CommonService;
import com.mongs.common.vo.FindVersionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/version")
    public ResponseEntity<FindVersionResDto> findNewestVersion(@RequestParam(value = "buildVersion", defaultValue = "0.0.0") String buildVersion) {
        FindVersionVo findVersionVo = commonService.findVersion(buildVersion);

        return ResponseEntity.ok().body(FindVersionResDto.builder()
                .newestBuildVersion(findVersionVo.newestBuildVersion())
                .createdAt(findVersionVo.createdAt())
                .mustUpdateApp(findVersionVo.mustUpdateApp())
                .mustUpdateCode(findVersionVo.mustUpdateCode())
                .build());
    }

    @GetMapping("/code")
    public ResponseEntity<FindCodeResDto> findCode() {
        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .mapCodeList(commonService.findMapCode())
                        .mongCodeList(commonService.findMongCode())
                        .foodCodeList(commonService.findFoodCode())
                        .feedbackCodeList(commonService.findFeedbackCode())
                        .build());
    }
}
