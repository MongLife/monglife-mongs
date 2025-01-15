package com.monglife.mongs.app.user.player.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.player.dto.request.CreateDeviceRequestDto;
import com.monglife.mongs.app.user.player.dto.request.UpdateWalkingCountRequestDto;
import com.monglife.mongs.app.user.player.dto.response.UpdateWalkingCountResponseDto;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import com.monglife.mongs.app.user.player.service.PlayerDeviceService;
import com.monglife.mongs.app.user.player.vo.PlayerStepVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/open/device")
@RequiredArgsConstructor
public class PlayerDeviceOpenController {

    private final PlayerDeviceService playerDeviceService;

    @GetMapping
    public void test() {
        
    }

    /**
     * 플레이어 기기 등록
     * @param createDeviceRequestDto 걸음 수 Dto
     * @return 성공 응답
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createDevice(@RequestBody CreateDeviceRequestDto createDeviceRequestDto) {

        String deviceId = createDeviceRequestDto.getDeviceId();
        Integer totalWalkingCount = createDeviceRequestDto.getTotalWalkingCount();
        LocalDateTime deviceBootedAt = createDeviceRequestDto.getDeviceBootedDt();
        String fcmToken = createDeviceRequestDto.getFcmToken();

        playerDeviceService.createDevice(deviceId, totalWalkingCount, deviceBootedAt, fcmToken);

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_CREATE_DEVICE.toResponseDto());
    }

    /**
     * 걸음 수 동기화 (실시간)
     * @param updateWalkingCountRequestDto 걸음 수 동기화 요청 Dto
     * @return 동기화 후 잔여 걸음 수 응답 Dto
     */
    @PatchMapping("/walking")
    public ResponseEntity<ResponseDto<UpdateWalkingCountResponseDto>> updateWalkingCount(@RequestBody UpdateWalkingCountRequestDto updateWalkingCountRequestDto) {

        String deviceId = updateWalkingCountRequestDto.getDeviceId();
        Integer totalWalkingCount = updateWalkingCountRequestDto.getTotalWalkingCount();
        LocalDateTime deviceBootedDt = updateWalkingCountRequestDto.getDeviceBootedDt();

        PlayerStepVo playerStepVo = playerDeviceService.updateWalkingCount(deviceId, totalWalkingCount, deviceBootedDt);

        UpdateWalkingCountResponseDto updateWalkingCountResponseDto = UpdateWalkingCountResponseDto.builder()
                .consumeWalkingCount(playerStepVo.getConsumeWalkingCount())
                .walkingCount(playerStepVo.getWalkingCount())
                .build();

        return ResponseEntity.ok(PlayerResponse.APP_USER_PLAYER_UPDATE_WALKING_COUNT.toResponseDto(updateWalkingCountResponseDto));
    }
}
