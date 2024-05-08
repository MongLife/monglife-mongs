package com.mongs.play.domain.mong.service;

import com.mongs.play.module.code.service.CodeService;
import com.mongs.play.domain.mong.entity.MongFeedLog;
import com.mongs.play.domain.mong.repository.MongFeedLogRepository;
import com.mongs.play.domain.mong.vo.MongFeedLogVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongFeedLogService {

    private final CodeService codeService;
    private final MongFeedLogRepository mongFeedLogRepository;

    public List<MongFeedLogVo> getFeedLog(Long mongId) {

        Map<String, MongFeedLog> mongFeedLogMap = mongFeedLogRepository.findByMongId(mongId).stream()
                .collect(Collectors.toMap(MongFeedLog::getCode, mongFeedLog -> mongFeedLog));

        List<MongFeedLogVo> mongFeedLogVoList = MongFeedLogVo.toList(codeService.getFoodCode());

        return mongFeedLogVoList.stream()
                .map(mongFeedLogVo -> {
                    if (mongFeedLogMap.containsKey(mongFeedLogVo.code())) {
                        return mongFeedLogVo.toBuilder()
                                .lastBuyAt(mongFeedLogMap.get(mongFeedLogVo.code()).getCreatedAt())
                                .build();
                    } else {
                        return mongFeedLogVo;
                    }
                })
                .toList();
    }
}
