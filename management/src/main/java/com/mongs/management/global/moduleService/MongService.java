package com.mongs.management.global.moduleService;

import com.mongs.management.global.entity.Mong;

import java.util.List;

public interface MongService {
    Mong getMong(Long mongId, Long accountId);
    List<Mong> getAllMong(Long accountId);
    Mong saveMong(Mong mong);
}
