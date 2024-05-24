package com.mongs.play.app.auth.internal.controller;

import com.mongs.play.app.auth.internal.dto.req.PassportReqDto;
import com.mongs.play.app.auth.internal.dto.res.PassportResDto;
import com.mongs.play.app.auth.internal.service.AuthInternalService;
import com.mongs.play.core.vo.PassportVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/internal/auth")
@RequiredArgsConstructor
@RestController
public class AuthInternalController {

    private final AuthInternalService authInternalService;

    /**
     * Passport 발급을 진행한다.
     *
     * @param passportReqDto 엑세스 토큰
     * @return Passport 발급에 성공하는 경우 {@link PassportVo}를 반환한다.
     */
    @PostMapping("/passport")
    public ResponseEntity<PassportResDto> passport(@RequestBody @Validated PassportReqDto passportReqDto) {

        PassportVo passportVo = authInternalService.passport(passportReqDto.accessToken());

        return ResponseEntity.ok().body(PassportResDto.builder()
                .data(passportVo.data())
                .passportIntegrity(passportVo.passportIntegrity())
                .build());
    }
}
