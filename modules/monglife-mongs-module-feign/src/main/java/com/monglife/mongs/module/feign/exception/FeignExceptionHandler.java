package com.monglife.mongs.module.feign.exception;

import com.monglife.core.dto.response.ResponseDto;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class FeignExceptionHandler {

    /**
     * exception handler
     * @param e 예외 객체
     * @return 에러 응답 객체
     */
    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ResponseDto<Map<String, Object>>> handleFeignClientException(FeignClientException e) {

        ResponseDto<Map<String, Object>> responseDto = new ResponseDto<>(e.getCode(), e.getMessage(), e.getHttpStatus(), e.getResult());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(responseDto);
    }

//    @ExceptionHandler(RetryableException.class)
//    public ResponseEntity<ResponseDto<Map<String, Object>>> handleRetryableException(RetryableException e) {
//
//        /*
//        Request processing failed: feign.RetryableException: Connect to http://pool-100-0-0-6.bstnma.fios.verizon.net:8030 [pool-100-0-0-6.bstnma.fios.verizon.net/100.0.0.6] failed: Connection refused executing PATCH http://MONGS-MANAGER/manager/internal/management/health
//         */
//
//        // TODO: CONNECTION FAIL 에 대한 예외처리
//
//        return
//    }
}
