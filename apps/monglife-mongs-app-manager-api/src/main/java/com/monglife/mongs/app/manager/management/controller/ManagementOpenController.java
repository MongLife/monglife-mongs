package com.monglife.mongs.app.manager.management.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.manager.management.dto.response.GetMinimalMongResponseDto;
import com.monglife.mongs.app.manager.management.enums.ManagementResponse;
import com.monglife.mongs.app.manager.management.service.ManagementService;
import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/open/management")
@RequiredArgsConstructor
public class ManagementOpenController {

    private final ManagementService managementService;

    /**
     * 몽 단건 조회
     * @param mongId 몽 ID
     * @return 몽 조회 응답 DTO
     */
    @GetMapping("/{mongId}")
    public ResponseEntity<ResponseDto<GetMinimalMongResponseDto>> getMong(@PathVariable("mongId") Long mongId) {

        MongVo mongVo = managementService.getMong(mongId);

        GetMinimalMongResponseDto getMinimalMongResponseDto = GetMinimalMongResponseDto.of(mongVo);

        return ResponseEntity.ok(ManagementResponse.APP_MANAGER_MANAGEMENT_GET_MONG.toResponseDto(getMinimalMongResponseDto));
    }
}
