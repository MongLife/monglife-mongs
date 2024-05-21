package com.mongs.play.module.feign.service;

import com.mongs.play.module.feign.client.PlayerInternalClient;
import com.mongs.play.module.feign.dto.req.IncreaseStarPointReqDto;
import com.mongs.play.module.feign.dto.res.IncreaseStarPointResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInternalMemberFeignService {

    private final PlayerInternalClient playerInternalClient;

    public IncreaseStarPointResDto increaseStarPointByRegisterMapCollection(Long accountId, Integer addStarPoint) {
        try {
            var res = playerInternalClient.increaseStarPointByRegisterMapCollection(IncreaseStarPointReqDto.builder()
                    .accountId(accountId)
                    .addStarPoint(addStarPoint)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public IncreaseStarPointResDto increaseStarPointByRegisterMongCollection(Long accountId, Integer addStarPoint) {
        try {
            var res = playerInternalClient.increaseStarPointByRegisterMongCollection(IncreaseStarPointReqDto.builder()
                    .accountId(accountId)
                    .addStarPoint(addStarPoint)
                    .build());

            return res.getBody();

        } catch (Exception e) {
            return null;
        }
    }
}
