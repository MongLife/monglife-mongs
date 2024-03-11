package com.mongs.lifecycle.config;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.repository.MongEventRepository;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MongRepository mongRepository;
    private final MongEventRepository mongEventRepository;

    @Override
    public void run(ApplicationArguments args) {
        mongRepository.save(Mong.builder()
                .accountId(1L)
                .name("테스트 몽")
                .build());

        mongEventRepository.deleteAll();
        for (int i = 0; i < MongEventCode.values().length; i++) {
            var now = LocalDateTime.now();
            mongEventRepository.save(MongEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventCode(MongEventCode.values()[i])
                    .mongId(1L)
                    .expiredAt(now.plusSeconds(10))
                    .createdAt(now)
                    .build());
        }
    }
}
