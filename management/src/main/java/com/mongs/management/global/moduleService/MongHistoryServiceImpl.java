package com.mongs.management.global.moduleService;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.entity.MongHistory;
import com.mongs.management.global.repository.MongHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongHistoryServiceImpl implements MongHistoryService {

    private final MongHistoryRepository mongHistoryRepository;

    @Override
    public MongHistory saveMongHistory(Long mongId, MongHistoryCode mongHistoryCode) {
        return mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .historyCode(mongHistoryCode)
                .message(mongHistoryCode.getMessage())
                .build());
    }
}
