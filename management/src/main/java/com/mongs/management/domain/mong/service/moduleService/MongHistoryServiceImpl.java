package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.core.enums.management.MongActive;
import com.mongs.management.domain.mong.entity.MongHistory;
import com.mongs.management.domain.mong.repository.MongHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongHistoryServiceImpl implements MongHistoryService {

    private final MongHistoryRepository mongHistoryRepository;

    @Override
    public MongHistory saveMongHistory(Long mongId, MongActive mongActive) {
        return mongHistoryRepository.save(MongHistory.builder()
                .mongId(mongId)
                .code(mongActive)
                .message(mongActive.getMessage())
                .build());
    }
}
