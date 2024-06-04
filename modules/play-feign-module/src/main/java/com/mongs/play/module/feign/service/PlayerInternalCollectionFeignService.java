package com.mongs.play.module.feign.service;

import com.mongs.play.module.feign.client.PlayerInternalClient;
import com.mongs.play.module.feign.dto.req.RegisterMapCollectionReqDto;
import com.mongs.play.module.feign.dto.req.RegisterMongCollectionReqDto;
import com.mongs.play.module.feign.dto.req.RemoveMapCollectionReqDto;
import com.mongs.play.module.feign.dto.req.RemoveMongCollectionReqDto;
import com.mongs.play.module.feign.dto.res.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInternalCollectionFeignService {

    private final PlayerInternalClient playerInternalClient;

    public RegisterMapCollectionResDto registerMapCollection(Long accountId, String mapCode) {
        var res = playerInternalClient.registerMapCollection(RegisterMapCollectionReqDto.builder()
                .accountId(accountId)
                .mapCode(mapCode)
                .build());

        return res.getBody();
    }

    public RegisterMongCollectionResDto registerMongCollection(Long accountId, String mongCode) {
        var res = playerInternalClient.registerMongCollection(RegisterMongCollectionReqDto.builder()
                .accountId(accountId)
                .mongCode(mongCode)
                .build());

        return res.getBody();
    }

    public RemoveMapCollectionResDto removeMapCollection(Long accountId, String mapCode) {
        var res = playerInternalClient.removeMapCollection(RemoveMapCollectionReqDto.builder()
                .accountId(accountId)
                .mapCode(mapCode)
                .build());

        return res.getBody();
    }

    public RemoveMongCollectionResDto removeMongCollection(Long accountId, String mongCode) {
        var res = playerInternalClient.removeMongCollection(RemoveMongCollectionReqDto.builder()
                .accountId(accountId)
                .mongCode(mongCode)
                .build());

        return res.getBody();
    }
}
