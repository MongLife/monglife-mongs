package com.mongs.play.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.core.dto.res.ErrorResDto;
import com.mongs.play.core.error.module.FeignErrorCode;
import com.mongs.play.core.exception.client.ClientErrorException;
import com.mongs.play.core.exception.module.ModuleErrorException;
import com.mongs.play.core.exception.module.NotAcceptableException;
import com.mongs.play.core.exception.module.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            ErrorResDto errorResDto = objectMapper.readValue(response.body().asInputStream(), ErrorResDto.class);
            log.info("httpStatus: {}, body: {}", response.status(), errorResDto);
        } catch (IOException ignored) {}

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
