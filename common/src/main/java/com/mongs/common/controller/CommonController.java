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
    public ResponseEntity<FindVersionResDto> findNewestVersion() {

        FindVersionVo findVersionVo = commonService.findVersion();

        return ResponseEntity.ok().body(FindVersionResDto.builder()
                .version(findVersionVo.version())
                .createdAt(findVersionVo.createdAt())
                .build());
    }

    @GetMapping("/code")
    public ResponseEntity<FindCodeResDto> findCode(@RequestParam(value = "version", defaultValue = "0") Long version) {
        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .mapCodeList(commonService.findMapCode(version))
                        .mongCodeList(commonService.findMongCode(version))
                        .foodCodeList(commonService.findFoodCode(version))
                        .feedbackCodeList(commonService.findFeedbackCode(version))
                        .build());
    }
}
