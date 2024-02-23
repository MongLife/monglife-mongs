package com.mongs.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class HmacProvider {

    private final ObjectMapper objectMapper;
    private final String secretKey;

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
        System.out.println("secretKey: " + secretKey + " / gen: " + generateIntegrity + " / ver: " + integrity);
        return integrity.equals(generateIntegrity);
    }
}
