package com.mongs.management.domain.mong.service.moduleService;

import com.mongs.management.domain.mong.entity.Mong;

import java.util.List;

public interface MongService {
    Mong getMong(Long mongId, Long accountId);
    List<Mong> getAllMong(Long accountId);
    Mong saveMong(Mong mong);
}
