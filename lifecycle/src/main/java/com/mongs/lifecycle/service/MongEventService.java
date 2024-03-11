package com.mongs.lifecycle.service;

import com.mongs.lifecycle.repository.MongEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongEventService {

    private final MongEventRepository mongEventRepository;

    @Transactional
    public void registerWeightDownEvent(Long mongId) {

    }

    @Transactional
    public void registerStrengthDownEvent(Long mongId) {

    }

    @Transactional
    public void registerSatietyDownEvent(Long mongId) {

    }

    @Transactional
    public void registerHealthyDownEvent(Long mongId) {

    }

    @Transactional
    public void registerSleepDownEvent(Long mongId) {

    }

    @Transactional
    public void registerSleepUpEvent(Long mongId) {

    }

    @Transactional
    public void registerPayPointUpEvent(Long mongId) {

    }

    @Transactional
    public void registerGeneratePoopEvent(Long mongId) {

    }
}
