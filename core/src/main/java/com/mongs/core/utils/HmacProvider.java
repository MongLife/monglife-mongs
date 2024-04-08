package com.mongs.core.utils;

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

    /**
     * data 에서 Hmac 해싱 값을 추출한다.
     * secretKey 는 사전에 정의된 key 를 사용한다.
     * 
     * @param data 해싱 값 추출하기 위한 데이터
     * @return Hmac 해싱 값, 생성에 실패하는 경우 Optional.empty() 값을 반환한다.
     */
    public Optional<String> generateHmac(Object data) {
        return this.generateHmac(data, this.secretKey);
    }


    /**
     * data 에서 Hmac 해싱 값을 추출한다.
     * secretKey 는 파라미터로 주어지는 값을 사용한다.
     *
     * @param data 해싱 값 추출하기 위한 데이터
     * @return Hmac 해싱 값, 생성에 실패하는 경우 Optional.empty() 값을 반환한다.
     */
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

    /**
     * data 와 integrity 값을 비교하여 데이터 무결성을 검증한다.
     *
     * @param data 검증할 데이터
     * @param integrity 검증할 해싱 값
     * @return 무결성 여부
     */
    public boolean verifyHmac(Object data, String integrity) {
        AtomicBoolean verify = new AtomicBoolean(false);

        generateHmac(data).ifPresent(generateIntegrity -> verify.set(integrity.equals(generateIntegrity)));

        return verify.get();
    }
}
