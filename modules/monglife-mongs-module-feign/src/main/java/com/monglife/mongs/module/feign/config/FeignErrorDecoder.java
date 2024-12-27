package com.monglife.mongs.module.feign.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.module.feign.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {

        ResponseDto<Map<String, Object>> responseDto;

        log.info("{} ===> {}", response, response.request());

        try {
            responseDto = objectMapper.readValue(response.body().asInputStream(), new TypeReference<>() {});
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        throw new FeignClientException(responseDto);
    }
}