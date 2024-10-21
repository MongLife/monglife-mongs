package com.monglife.mongs.app.management.service.business;

import com.monglife.mongs.app.management.business.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class ManagerService {
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FindMongVo> getMong(Long accountId) {
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public FindMongVo getMongs(Long accountId, Long mongId) {
        return FindMongVo.builder().build();
    }

    @Transactional
    public RegisterMongVo createMong(Long accountId, String name, String sleepStart, String sleepEnd) {
        return RegisterMongVo.builder().build();
    }

    @Transactional
    public DeleteMongVo deleteMong(Long accountId, Long mongId) {
        return DeleteMongVo.builder().build();
    }
}
