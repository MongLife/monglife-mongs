package com.mongs.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
public class HmacProvider {

    private final ObjectMapper objectMapper;
    private final String secretKey;

    public Optional<String> generateHmac(Object data) {
        return this.generateHmac(data, this.secretKey);
    }

    public Optional<String> generateHmac(Object data, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

            byte[] hash = mac.doFinal(objectMapper.writeValueAsBytes(data));
            return Optional.of(Base64.encodeBase64String(hash));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean verifyHmac(Object data, String integrity) {
        AtomicBoolean verify = new AtomicBoolean(false);

        generateHmac(data).ifPresent(generateIntegrity -> verify.set(integrity.equals(generateIntegrity)));

        return verify.get();
    }
}
