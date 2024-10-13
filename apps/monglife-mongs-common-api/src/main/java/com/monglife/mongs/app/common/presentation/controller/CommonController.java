package com.monglife.mongs.app.common.presentation.controller;

import com.monglife.mongs.app.common.business.service.CommonService;
import com.monglife.mongs.app.common.business.vo.FindCodeVersionVo;
import com.monglife.mongs.app.common.business.vo.FindCodeVo;
import com.monglife.mongs.app.common.presentation.dto.req.FindCodeReqDto;
import com.monglife.mongs.app.common.presentation.dto.req.FindCodeVersionReqDto;
import com.monglife.mongs.app.common.presentation.dto.res.FindCodeResDto;
import com.monglife.mongs.app.common.presentation.dto.res.FindCodeVersionResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/common")
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonService commonService;

    /**
     * 앱 빌드 버전에 따른 업데이트 내용을 확인한다.
     *
     * @param findCodeVersionReqDto 앱 빌드 버전, 앱 내 코드 리스트 해싱 값
     * @return {@link FindCodeVersionResDto}
     */
    @PostMapping("/version")
    public ResponseEntity<FindCodeVersionResDto> findCodeVersion(@RequestBody FindCodeVersionReqDto findCodeVersionReqDto) {

        FindCodeVersionVo findCodeVersionVo = commonService.findCodeVersion(
                findCodeVersionReqDto.buildVersion(),
                findCodeVersionReqDto.codeIntegrity());

        return ResponseEntity.ok().body(FindCodeVersionResDto.builder()
                .newestBuildVersion(findCodeVersionVo.newestBuildVersion())
                .createdAt(findCodeVersionVo.createdAt())
                .updateApp(findCodeVersionVo.updateApp())
                .updateCode(findCodeVersionVo.updateCode())
                .build());
    }

    /**
     * 앱 빌드 버전에 해당하는 맵, 몽, 음식, 피드백에 대한 코드 목록을 조회한다.
     *
     * @param findCodeReqDto 앱 빌드 버전
     * @return {@link FindCodeResDto}
     */
    @PostMapping("/code")
    public ResponseEntity<FindCodeResDto> findCode(@RequestBody FindCodeReqDto findCodeReqDto) {

        FindCodeVo findCodeVo = commonService.findCode(findCodeReqDto.buildVersion());

        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .codeIntegrity(findCodeVo.codeIntegrity())
                        .mapCodeList(findCodeVo.mapCodeList())
                        .mongCodeList(findCodeVo.mongCodeList())
                        .foodCodeList(findCodeVo.foodCodeList())
                        .build());
    }
}
