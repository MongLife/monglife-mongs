package com.mongs.play.module.kafka.aspect;

import com.mongs.play.module.kafka.event.BasicEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaListenerAspect {

    private final KafkaService kafkaService;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(kafkaListener)")
    public Object around(ProceedingJoinPoint joinPoint, KafkaListener kafkaListener) {

        Object returnValue = null;

        try {
            returnValue = joinPoint.proceed();
        } catch (Throwable e) {
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof BasicEvent) {
                    for (String topic : kafkaListener.topics()) {
                        BasicEvent event = (BasicEvent) arg;
                        kafkaService.sendRollback(topic.replace("commit.", ""), event);
                    }
                }
            }
        }

        return returnValue;
    }
}
