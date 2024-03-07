//package com.mongs.lifecycle.service_.task;
//
//import com.mongs.lifecycle.code.EventStatusCode;
//import com.mongs.lifecycle.code.MongEventCode;
//import com.mongs.lifecycle.entity.Mong;
//import com.mongs.lifecycle.entity.MongEvent;
//import com.mongs.lifecycle.exception.EventTaskException;
//import com.mongs.lifecycle.exception.LifecycleErrorCode;
//import com.mongs.lifecycle.repository.EventTaskRepository;
//import com.mongs.lifecycle.repository.MongEventRepository;
//import com.mongs.lifecycle.repository.MongRepository;
//import com.mongs.lifecycle.thread.BasicTask;
//import com.mongs.lifecycle.thread.HealthyDownTask;
//import com.mongs.lifecycle.utils.MongEventUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class HealthyTaskService {
//
//    private final MongEventUtil mongEventUtil;
//    private final MongRepository mongRepository;
//    private final MongEventRepository mongEventRepository;
//    private final EventTaskRepository eventTaskRepository;
//
//    @Transactional
//    public void registerTask(Long mongId) {
//        BasicTask basicTask = HealthyDownTask.of(this, mongEventUtil.generateMongEvent(mongId, MongEventCode.HEALTHY_DOWN));
//
//        try {
//            mongEventRepository.save(basicTask.getEvent());
//            eventTaskRepository.save(basicTask);
//            basicTask.start();
//        } catch (RuntimeException e) {
//            log.error("[registerTask] {} : {} Task 재실행 실패", mongId, MongEventCode.HEALTHY_DOWN);
//        }
//    }
//
//    @Transactional
//    public void setStatusCode(String eventId, EventStatusCode statusCode) throws RuntimeException {
//        MongEvent mongEvent = mongEventRepository.findById(eventId)
//                        .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_TASK));
//        mongEventRepository.save(mongEvent.toBuilder().statusCode(statusCode).build());
//    }
//
//    @Transactional
//    public void decreaseValue(MongEvent event) throws RuntimeException {
//        Mong mong = mongRepository.findById(event.getMongId())
//                .orElseThrow(() -> new EventTaskException(LifecycleErrorCode.NOT_FOUND_MONG));
//
//        MongEventCode code = event.getEventCode();
//        long seconds = Math.min(code.getExpiration(), Duration.between(event.getCreatedAt(), LocalDateTime.now()).getSeconds());
//        double subHealthy = code.getValue() / code.getExpiration() * seconds;
//        double newHealthy = Math.max(0D, mong.getHealthy() - subHealthy);
//
//        log.info("[healthyDownEvent] 체력 {} 감소, mongEvent: {}", mong.getHealthy() - newHealthy, event);
//
//        mongRepository.save(mong.toBuilder()
//                .healthy(newHealthy)
//                .build());
//    }
//}
