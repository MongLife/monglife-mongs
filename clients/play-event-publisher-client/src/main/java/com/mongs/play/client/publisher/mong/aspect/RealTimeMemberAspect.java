//package com.mongs.play.client.publisher.mong.aspect;
//
//import com.mongs.play.client.publisher.mong.annotation.RealTimeMember;
//import com.mongs.play.client.publisher.mong.code.PublishCode;
//import com.mongs.play.client.publisher.mong.service.MqttService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//
//import java.lang.reflect.Field;
//
//@Slf4j
//@Order(1)
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class RealTimeMemberAspect {
//
//    private final MqttService mqttService;
//
//    @Pointcut("execution(* com.mongs.play..*.*(..))")
//    public void cut() {}
//
//    @Around(value = "cut() && @annotation(annotation)")
//    public Object notification(ProceedingJoinPoint joinPoint, RealTimeMember annotation) throws Throwable {
//        Object returnValue = joinPoint.proceed();
//
//        Object responseBody = returnValue;
//
//        if (returnValue instanceof ResponseEntity<?>) {
//            responseBody = ((ResponseEntity<?>) returnValue).getBody();
//        }
//
//        if (!ObjectUtils.isEmpty(responseBody)) {
//            this.notification(responseBody, annotation.code());
//        }
//
//        return returnValue;
//    }
//
//    private void notification(Object body, PublishCode code) {
//        Field[] declaredFields = body.getClass().getDeclaredFields();
//        for (Field field : declaredFields) {
//            field.setAccessible(true);
//            try {
//                if (field.getName().equals("accountId")) {
//                    Object fieldValue = field.get(body);
//                    if (!ObjectUtils.isEmpty(fieldValue)) {
//                        Long accountId = (Long) fieldValue;
//                        mqttService.send(code, accountId, body);
//                    }
//                }
//            } catch (IllegalAccessException ignored) {}
//        }
//    }
//}
