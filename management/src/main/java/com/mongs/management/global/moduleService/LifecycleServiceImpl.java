package com.mongs.management.global.moduleService;

import com.mongs.management.domain.management.client.LifecycleClient;
import com.mongs.management.domain.management.client.dto.response.*;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifecycleServiceImpl implements LifecycleService {

    private final LifecycleClient lifecycleClient;

    @Override
    public Optional<EggMongEventResDto> eggMongEvent(Long mongId) {

        try {
            ResponseEntity<EggMongEventResDto> response = lifecycleClient.eggMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<DeleteMongEventResDto> deleteMongEvent(Long mongId) {
        try {
            ResponseEntity<DeleteMongEventResDto> response = lifecycleClient.deleteMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<SleepMongEventResDto> sleepMongEvent(Long mongId) {
        try {
            ResponseEntity<SleepMongEventResDto> response =  lifecycleClient.sleepMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<WakeupMongEventResDto> wakeUpMongEvent(Long mongId) {
        try {
            ResponseEntity<WakeupMongEventResDto> response = lifecycleClient.wakeupMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<GraduationMongEventResDto> graduateReadyMongEvent(Long mongId) {
        try {
            ResponseEntity<GraduationMongEventResDto> response = lifecycleClient.graduationReadyMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<EvolutionMongEventResDto> eggEvolutionMongEvent(Long mongId) {
        try {
            ResponseEntity<EvolutionMongEventResDto> response = lifecycleClient.eggEvolutionMongEvent(mongId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }

        } catch (FeignException e) {
            log.error("[{}] 통신 실패 : {}", mongId, e.getMessage());
        }

        return Optional.empty();
    }
}
