package com.mongs.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HttpUtils {
    private final ObjectMapper objectMapper;

    public Optional<String> getHeader(ServerHttpRequest request, String key) {
        List<String> values = request.getHeaders().get(key);

        if (values != null && !values.isEmpty()) {
            return Optional.of(values.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getJsonString(Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            return Optional.of(json);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
