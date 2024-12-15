package com.monglife.mongs.app.manager.management.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ManagementResponse implements Response {

    /**
     * 성공 응답
     */
    // MANAGER - MANAGEMENT
    MANAGER_MANAGEMENT_GET_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 정보 조회를 성공했습니다."),
    MANAGER_MANAGEMENT_CREATE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-001", "몽 생성을 성공했습니다."),
    MANAGER_MANAGEMENT_GET_FEED_ITEM(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-002", "먹이 목록 조회를 성공했습니다."),
    MANAGER_MANAGEMENT_DELETE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-003", "몽 삭제를 성공했습니다."),
    MANAGER_MANAGEMENT_FEED_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-004", "몽 먹이 주기를 성공했습니다."),
    MANAGER_MANAGEMENT_STROKE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-005", "몽 쓰다 듬기를 성공했습니다."),
    MANAGER_MANAGEMENT_SLEEP_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-006", "몽 수면/기상 상태로 변경했습니다."),
    MANAGER_MANAGEMENT_POOP_CLEAN_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-007", "몽 배변 처리를 성공했습니다."),
    MANAGER_MANAGEMENT_EVOLUTION_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-008", "몽 진화에 성공했습니다."),
    MANAGER_MANAGEMENT_GRADUATE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-009", "몽 졸업에 성공했습니다."),
    MANAGER_MANAGEMENT_OBSERVE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-010", "몽 정보에 변동이 있습니다."),
    MANAGER_MANAGEMENT_OBSERVE_MONG_STATE(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-011", "몽 상태 정보에 변동이 있습니다."),
    MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-012", "몽 지수 정보에 변동이 있습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto() {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }

}
