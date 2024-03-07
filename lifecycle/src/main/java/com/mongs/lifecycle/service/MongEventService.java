//package com.mongs.lifecycle.service;
//
//import com.mongs.lifecycle.code.EventStatusCode;
//import com.mongs.lifecycle.code.MongEventCode;
//import com.mongs.lifecycle.entity.MongEvent;
//import com.mongs.lifecycle.repository.MongEventRepository;
//import com.mongs.lifecycle.thread.DeadTask;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MongEventService {
//
////    private final MongEventActiveService mongEventActiveService;
//    private final MongEventRepository mongEventRepository;
//
//    private final Integer MIN_POOP_EXPIRATION = 5;//60 * 60 * 4;
//
//    private Long getExpiration(MongEventCode mongEventCode) {
//        long expiration = mongEventCode.getExpiration();
//
//        if (MongEventCode.POOP.equals(mongEventCode)) {
//            expiration = (int) (Math.random() * (expiration - MIN_POOP_EXPIRATION + 1) + MIN_POOP_EXPIRATION);
//        }
//
//        return expiration;
//    }
//
//    private void registerTask(MongEvent event) throws RuntimeException {
//        switch (event.getEventCode()) {
//            case WEIGHT_DOWN -> DeadTask.of(event).start();
//            case STRENGTH_DOWN -> DeadTask.of(event).start();
//            case SATIETY_DOWN -> DeadTask.of(event).start();
//            case HEALTHY_DOWN -> DeadTask.of(event).start();
//            case SLEEP_DOWN -> DeadTask.of(event).start();
//            case SLEEP_UP -> DeadTask.of(event).start();
//            case PAY_POINT_UP -> DeadTask.of(event).start();
//            case POOP -> DeadTask.of(event).start();
//            case DEAD, ADMIN_DEAD -> DeadTask.of(event).start();
//            default -> throw new RuntimeException();
//        }
//    }
//
//
//    @Transactional
//    public void registerMongEvent(Long mongId, MongEventCode mongEventCode) throws RuntimeException {
//        long expiration = getExpiration(mongEventCode);
//
//        LocalDateTime createdAt = LocalDateTime.now();
//        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);
//
//        MongEvent mongEvent = mongEventRepository.save(MongEvent.builder()
//                                    .eventId(UUID.randomUUID().toString())
//                                    .mongId(mongId)
//                                    .eventCode(mongEventCode)
//                                    .expiration(expiration)
//                                    .expiredAt(expiredAt)
//                                    .createdAt(createdAt)
//                                    .statusCode(EventStatusCode.WAIT)
//                                    .build());
//        registerTask(mongEvent);
//    }
//
//    @Transactional
//    public void removeMongEvent(Long mongId, MongEventCode mongEventCode) throws RuntimeException {
////        mongEventRepository.findByMongIdAndEventCode(mongId, mongEventCode)
////                .ifPresent(event -> {
////                    this.updateMong(event);
////                    mongEventRepository.deleteByMongIdAndEventCode(mongId, mongEventCode);
////                });
//    }
//
//    @Transactional
//    public void removeAllMongEvent(Long mongId) throws RuntimeException {
////        mongEventRepository.findByMongId(mongId)
////                .forEach(event -> {
////                    this.updateMong(event);
////                    mongEventRepository.deleteByMongIdAndEventCode(mongId, event.getEventCode());
////                });
//    }
//}
