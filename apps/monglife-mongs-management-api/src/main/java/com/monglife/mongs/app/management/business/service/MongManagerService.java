package com.monglife.mongs.app.management.business.service;

import com.monglife.mongs.app.management.business.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MongManagerService {
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FindMongVo> findMong(Long accountId) {
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public FindMongVo findMong(Long accountId, Long mongId) {
        return FindMongVo.builder().build();
    }

    @Transactional
    public RegisterMongVo registerMong(Long accountId, String name, String sleepStart, String sleepEnd) {
        return RegisterMongVo.builder().build();
    }

    @Transactional
    public DeleteMongVo deleteMong(Long accountId, Long mongId) {
        return DeleteMongVo.builder().build();
    }
}
