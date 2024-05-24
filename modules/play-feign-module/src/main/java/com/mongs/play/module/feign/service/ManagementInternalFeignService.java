package com.mongs.play.module.feign.service;

import com.mongs.play.module.feign.client.ManagementInternalClient;
import com.mongs.play.module.feign.dto.req.*;
import com.mongs.play.module.feign.dto.res.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementInternalFeignService {

    private final ManagementInternalClient managementInternalClient;

    public IncreasePayPointResDto increasePayPoint(Long mongId, Integer addPayPoint) {
        var res = managementInternalClient.increasePayPoint(IncreasePayPointReqDto.builder()
                .mongId(mongId)
                .addPayPoint(addPayPoint)
                .build());

        return res.getBody();
    }
}
