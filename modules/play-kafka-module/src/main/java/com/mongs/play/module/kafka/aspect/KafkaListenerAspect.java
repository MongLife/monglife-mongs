package com.mongs.play.module.kafka.aspect;

import com.mongs.play.module.kafka.event.rollback.EvolutionMongRollbackEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaListenerAspect {

    private final KafkaService kafkaService;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around("cut() && @annotation(kafkaListener)")
    public Object around(ProceedingJoinPoint joinPoint, KafkaListener kafkaListener) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            String[] topics = kafkaListener.topics();

            log.info("topics: {}", Arrays.toString(topics));

            kafkaService.sendRollback(KafkaService.KafkaTopic.EVOLUTION_MONG, EvolutionMongRollbackEvent.builder()
                    .id(event.getId())
                    .createdAt(event.getCreatedAt())
                    .accountId(accountId)
                    .mongCode(mongCode)
                    .build());
        }

        return new Object();
    }
}
