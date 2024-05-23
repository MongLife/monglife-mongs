package com.mongs.play.config;

import com.mongs.play.core.error.module.FeignErrorCode;
import com.mongs.play.core.exception.client.ClientErrorException;
import com.mongs.play.core.exception.module.ModuleErrorException;
import com.mongs.play.core.exception.module.NotAcceptableException;
import com.mongs.play.core.exception.module.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        log.info("httpStatus: {}, body: {}", response.status(), response.body());

        switch (HttpStatus.valueOf(response.status())) {
            case NOT_FOUND -> {
                return new NotFoundException(FeignErrorCode.NOT_FOUND_RESOURCE);
            }
            case NOT_ACCEPTABLE, BAD_REQUEST -> {
                return new NotAcceptableException(FeignErrorCode.NOT_ACCEPTABLE_CHANGE);
            }
            default -> {
                return new ModuleErrorException(FeignErrorCode.SERVICE_UNAVAILABLE);
            }
        }
    }
}