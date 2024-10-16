package com.mongs.play.app.common.controller;

import com.mongs.play.app.common.dto.req.FindCodeReqDto;
import com.mongs.play.app.common.dto.req.FindCodeVersionReqDto;
import com.mongs.play.app.common.dto.res.FindCodeResDto;
import com.mongs.play.app.common.dto.res.FindCodeVersionResDto;
import com.mongs.play.app.common.service.CommonExternalService;
import com.mongs.play.app.common.vo.FindCodeVersionVo;
import com.mongs.play.app.common.vo.FindCodeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/common")
@RequiredArgsConstructor
@RestController
public class CommonExternalController {

    private final CommonExternalService commonExternalService;

    /**
     * 앱 빌드 버전에 따른 업데이트 내용을 확인한다.
     *
     * @param findCodeVersionReqDto 앱 빌드 버전, 앱 내 코드 리스트 해싱 값
     * @return {@link FindCodeVersionResDto}
     */
    @PostMapping("/version")
    public ResponseEntity<FindCodeVersionResDto> findCodeVersion(@RequestBody FindCodeVersionReqDto findCodeVersionReqDto) {

        FindCodeVersionVo findCodeVersionVo = commonExternalService.findCodeVersion(
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

        FindCodeVo findCodeVo = commonExternalService.findCode(findCodeReqDto.buildVersion());

        return ResponseEntity.ok().body(
                FindCodeResDto.builder()
                        .codeIntegrity(findCodeVo.codeIntegrity())
                        .mapCodeList(findCodeVo.mapCodeList())
                        .mongCodeList(findCodeVo.mongCodeList())
                        .foodCodeList(findCodeVo.foodCodeList())
                        .build());
    }
}
