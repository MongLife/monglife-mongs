package com.mongs.play.module.kafka.aspect;

import com.mongs.play.module.kafka.annotation.KafkaRequestPayload;
import com.mongs.play.module.kafka.dto.KafkaEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaConsumerAspect {

    @Pointcut("execution(* com.mongs.play..*Consumer.*(..))")
    public void cut() {}

    @Pointcut("@annotation(com.mongs.play.module.kafka.annotation.KafkaRequestPayload)")
    private void enabledKafkaRequestPayload(){}

    @Before("cut() && enabledKafkaRequestPayload()")
    public void before(JoinPoint joinPoint) throws NoSuchFieldException {

        org.aspectj.lang.reflect.MethodSignature signature = (org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature();
        java.lang.reflect.Method method = signature.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();

        org.springframework.web.bind.annotation.RequestMapping ann =
                AnnotationUtils.findAnnotation(method, org.springframework.web.bind.annotation.RequestMapping.class);


        log.info("static part: {}", joinPoint.getSignature().getDeclaringType());

//        Object[] args = joinPoint.getArgs();
//
//        log.info("before call - args: {}", args);
//
//        for (Object arg : args) {
//            if (arg instanceof KafkaEventDto<?> kafkaEventDto) {
//                Field kafkaEventDtoDataField = kafkaEventDto.getClass().getDeclaredField("data");
//                ParameterizedType stringListType = (ParameterizedType) kafkaEventDtoDataField.getGenericType();
//                Class<?> kafkaEventDtoDataType = (Class<?>) stringListType.getActualTypeArguments()[0];
//                System.out.println(kafkaEventDtoDataType);
//            }
//        }
    }
}
