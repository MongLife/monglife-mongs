package com.mongs.lifecycle.service_;

import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.entity.MongEvent;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import com.mongs.lifecycle.repository.MongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActiveService {

    private final MongRepository mongRepository;

    @Transactional
    public void decreaseHealthy(MongEvent event) throws RuntimeException {
        Mong mong = mongRepository.findById(event.getMongId())
                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));

        MongEventCode code = event.getEventCode();
        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
        double subHealthy = code.getValue() / code.getExpiration() * seconds;
        double newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);

        log.info("[healthyDownEvent] 체력 {} 감소, mongEvent: {}", mong.getHealthy() - newHealthy, event);

        mongRepository.save(mong.toBuilder()
                .healthy(newHealthy)
                .build());
    }
}
