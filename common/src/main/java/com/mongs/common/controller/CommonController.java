package com.mongs.common.controller;

import com.mongs.common.dto.response.FindCodeResDto;
import com.mongs.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final CommonService commonService;

    @GetMapping("")
    public ResponseEntity<Object> findCode(@RequestParam("version") String versionCode) {
        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .version(commonService.codeVersionCheckAndNewestCode(versionCode))
                        .mapCodeList(commonService.findMapCode())
                        .mongCodeList(commonService.findMongCode())
                        .foodCodeList(commonService.findFoodCode())
                        .build()
        );
    }

    @GetMapping("/map")
    public ResponseEntity<Object> findMapCode() {
        return ResponseEntity.ok().body(commonService.findMapCode());
    }

    @GetMapping("/mong")
    public ResponseEntity<Object> findMongCode() {
        return ResponseEntity.ok().body(commonService.findMongCode());
    }

    @GetMapping("/food")
    public ResponseEntity<Object> findFoodCode() {
        return ResponseEntity.ok().body(commonService.findFoodCode());
    }
}
