package com.mongs.management.global.moduleService;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.entity.MongHistory;

public interface MongHistoryService {
    MongHistory saveMongHistory(Long mongId, MongHistoryCode mongHistoryCode);
}
