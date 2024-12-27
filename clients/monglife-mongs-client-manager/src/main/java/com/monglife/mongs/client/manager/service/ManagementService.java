package com.monglife.mongs.client.manager.service;

import com.monglife.mongs.client.manager.client.ManagementClient;
import com.monglife.mongs.client.manager.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.client.manager.exception.ChargePayPointException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementClient managementClient;

    public void chargePayPoint(Long mongId, Integer payPoint) {

        try {
            ChargePayPointRequestDto chargePayPointRequestDto = ChargePayPointRequestDto.builder()
                    .mongId(mongId)
                    .payPoint(payPoint)
                    .build();

            managementClient.chargePayPoint(chargePayPointRequestDto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ChargePayPointException(mongId);
        }
    }
}
