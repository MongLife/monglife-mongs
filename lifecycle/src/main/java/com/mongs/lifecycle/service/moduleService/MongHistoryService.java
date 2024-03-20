package com.mongs.lifecycle.service.moduleService;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.entity.MongHistory;
import com.mongs.lifecycle.repository.MongHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongHistoryService {

    private final MongHistoryRepository mongHistoryRepository;

    public MongHistory saveMongHistory(Long mongId, MongHistoryCode mongHistoryCode) {
        return mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .historyCode(mongHistoryCode)
                .message(mongHistoryCode.getMessage())
                .build());
    }
}
