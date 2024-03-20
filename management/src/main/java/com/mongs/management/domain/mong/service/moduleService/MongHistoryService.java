package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.core.enums.management.MongActive;
import com.mongs.management.domain.mong.entity.MongHistory;

public interface MongHistoryService {
    MongHistory saveMongHistory(Long mongId, MongActive mongActive);
}
