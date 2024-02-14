package com.mongs.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class HmacProvider {

    private final ObjectMapper objectMapper;

    @Value("${application.security.hmac.secret-key}")
    private String secretKey;

    public String generateHmac(Object data) throws Exception {
        return this.generateHmac(data, this.secretKey);
    }

    public String generateHmac(Object data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

        byte[] hash = mac.doFinal(objectMapper.writeValueAsBytes(data));
        return Base64.encodeBase64String(hash);
    }

    public Boolean verifyHmac(Object data, String integrity) throws Exception {
        String generateIntegrity = generateHmac(data);
        return integrity.equals(generateIntegrity);
    }
}
