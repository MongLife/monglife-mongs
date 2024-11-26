package com.monglife.mongs.app.manager.global.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ManagerResponse implements Response {

    /**
     * 성공 응답
     */
    // MANAGER - MANAGEMENT
    MANAGER_MANAGEMENT_GET_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 정보 조회를 성공했습니다."),
    MANAGER_MANAGEMENT_CREATE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 생성을 성공했습니다."),
    MANAGER_MANAGEMENT_GET_FEED_ITEM(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "먹이 목록 조회를 성공했습니다."),
    MANAGER_MANAGEMENT_DELETE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 삭제를 성공했습니다."),
    MANAGER_MANAGEMENT_FEED_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 먹이 주기를 성공했습니다."),
    MANAGER_MANAGEMENT_STROKE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 쓰다 듬기를 성공했습니다."),
    MANAGER_MANAGEMENT_SLEEP_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 수면/기상 상태로 변경했습니다."),
    MANAGER_MANAGEMENT_POOP_CLEAN_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 배변 처리를 성공했습니다."),
    MANAGER_MANAGEMENT_EVOLUTION_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 진화에 성공했습니다."),
    MANAGER_MANAGEMENT_GRADUATE_MONG(HttpStatus.OK.value(), "MANAGER-MANAGEMENT-000", "몽 졸업에 성공했습니다."),


    /**
     * 실패 응답
     */
    MANAGER_MANAGEMENT_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-100", "몽 타입이 존재하지 않습니다."),
    MANAGER_MANAGEMENT_NOT_EXISTS_FOOD_TYPE(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-101", "음식 타입이 존재하지 않습니다."),
    MANAGER_MANAGEMENT_NOT_EXISTS_MONG(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-101", "몽이 존재하지 않습니다."),
    MANAGER_MANAGEMENT_INVALID_FEED(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-101", "현재 섭취가 불가능한 음식입니다."),
    MANAGER_MANAGEMENT_INVALID_EVOLUTION(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-101", "진화 가능한 상태가 압니다."),
    MANAGER_MANAGEMENT_INVALID_GRADUATE(HttpStatus.BAD_REQUEST.value(), "MANAGER-MANAGEMENT-101", "졸업 가능한 상태가 압니다."),


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
