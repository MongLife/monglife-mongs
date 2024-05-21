//package com.mongs.play.client.publisher.mong.aspect;
//
//import com.mongs.play.client.publisher.mong.annotation.RealTimeMember;
//import com.mongs.play.client.publisher.mong.annotation.RealTimeMong;
//import com.mongs.play.client.publisher.mong.code.PublishCode;
//import com.mongs.play.client.publisher.mong.dto.res.BasicPublishDto;
//import com.mongs.play.client.publisher.mong.service.MqttService;
//import com.mongs.play.client.publisher.mong.vo.*;
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
//import java.util.ArrayList;
//import java.util.List;
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
//    public Object realTimeMember(ProceedingJoinPoint joinPoint, RealTimeMember annotation) throws Throwable {
//        Object returnValue = joinPoint.proceed();
//
//        Object body = returnValue;
//
//        if (returnValue instanceof ResponseEntity<?>) {
//            body = ((ResponseEntity<?>) returnValue).getBody();
//        }
//
//        if (!ObjectUtils.isEmpty(body)) {
//
//            Long mongId = this.getAccountId(body);
//            List<Object> publishVoList = new ArrayList<>();
//
//            for (PublishCode publishCode : annotation.codes()) {
//                switch (publishCode) {
//                    case MEMBER_STAR_POINT -> {
//                        Long accountId = this.getAccountId(body);
//                        Object publishVo = this.setFields(new MemberStarPointVo(), body);
//                        mqttService.sendMember(accountId, publishVo);
//                    }
//                    default -> {}
//                }
//            }
//        }
//
//        return returnValue;
//    }
//
//    private Long getAccountId(Object body) {
//        Field[] bodyFields = body.getClass().getDeclaredFields();
//
//        for (Field bodyField : bodyFields) {
//            if (bodyField.getName().equals("accountId")) {
//                boolean isBodyFieldAccessible = bodyField.canAccess(body);
//                bodyField.setAccessible(true);
//
//                try {
//                    Object accountId = bodyField.get(body);
//                    return (Long) accountId;
//                } catch (IllegalAccessException ignored) {
//                } finally {
//                    bodyField.setAccessible(isBodyFieldAccessible);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private Object setFields(Object body, Object publishVo) {
//        Field[] publishVoFields = publishVo.getClass().getDeclaredFields();
//        Field[] bodyFields = body.getClass().getDeclaredFields();
//
//        for (Field publishVoField : publishVoFields) {
//            for (Field bodyField : bodyFields) {
//
//                if (publishVoField.getName().equals(bodyField.getName())) {
//                    boolean isBodyFieldAccessible = bodyField.canAccess(body);
//                    boolean isPublishVoFieldAccessible = publishVoField.canAccess(publishVo);
//
//                    bodyField.setAccessible(true);
//                    publishVoField.setAccessible(true);
//
//                    try {
//                        Object bodyFieldValue = bodyField.get(body);
//                        publishVoField.set(publishVo, bodyFieldValue);
//
//                    } catch (IllegalAccessException ignored) {}
//
//                    bodyField.setAccessible(isBodyFieldAccessible);
//                    publishVoField.setAccessible(isPublishVoFieldAccessible);
//
//                    break;
//                }
//            }
//        }
//
//        return publishVo;
//    }
//}