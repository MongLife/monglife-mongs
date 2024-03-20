package com.mongs.lifecycle.service.moduleService;

import com.mongs.core.enums.management.MongActive;
import com.mongs.lifecycle.entity.MongHistory;
import com.mongs.lifecycle.repository.MongHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongHistoryService {

    private final MongHistoryRepository mongHistoryRepository;

    public MongHistory saveMongHistory(Long mongId, MongActive mongActive) {
        return mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(mongActive)
                .message(mongActive.getMessage())
                .build());
    }
}
