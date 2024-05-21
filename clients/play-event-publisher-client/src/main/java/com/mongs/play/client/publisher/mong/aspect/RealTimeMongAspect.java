package com.mongs.play.client.publisher.mong.aspect;

import com.mongs.play.client.publisher.mong.annotation.RealTimeMong;
import com.mongs.play.client.publisher.mong.code.PublishCode;
import com.mongs.play.client.publisher.mong.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.mong.service.MqttService;
import com.mongs.play.client.publisher.mong.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(2)
@Aspect
@Component
@RequiredArgsConstructor
public class RealTimeMongAspect {

    private final MqttService mqttService;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(annotation)")
    public Object realTimeMong(ProceedingJoinPoint joinPoint, RealTimeMong annotation) throws Throwable {
        Object returnValue = joinPoint.proceed();

        Object body = returnValue;

        if (returnValue instanceof ResponseEntity<?>) {
            body = ((ResponseEntity<?>) returnValue).getBody();
        }

        if (!ObjectUtils.isEmpty(body)) {
            Long mongId = this.getMongId(body);
            List<Object> publishVoList = new ArrayList<>();

            for (PublishCode publishCode : annotation.codes()) {
                switch (publishCode) {
                    case MONG_CODE -> {
                        Object data = this.setFields(body, new MongCodeVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_STATUS -> {
                        Object data = this.setFields(body, new MongStatusVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_EXP -> {
                        Object data = this.setFields(body, new MongExpVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_POOP_COUNT -> {
                        Object data = this.setFields(body, new MongPoopCountVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_STATE -> {
                        Object data = this.setFields(body, new MongStateVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_SHIFT -> {
                        Object data = this.setFields(body, new MongShiftVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_PAY_POINT -> {
                        Object data = this.setFields(body, new MongPayPointVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_IS_SLEEPING -> {
                        Object data = this.setFields(body, new MongIsSleepingVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    default -> {}
                }
            }

            mqttService.sendMong(mongId, publishVoList);
        }

        return returnValue;
    }

    private Long getMongId(Object body) {
        Field[] bodyFields = body.getClass().getDeclaredFields();

        for (Field bodyField : bodyFields) {
            if (bodyField.getName().equals("mongId")) {
                boolean isBodyFieldAccessible = bodyField.canAccess(body);
                bodyField.setAccessible(true);

                try {
                    Object mongId = bodyField.get(body);
                    return (Long) mongId;
                } catch (IllegalAccessException ignored) {
                } finally {
                    bodyField.setAccessible(isBodyFieldAccessible);
                }
            }
        }

        return null;
    }

    private Object setFields(Object body, Object publishVo) {
        Field[] publishVoFields = publishVo.getClass().getDeclaredFields();
        Field[] bodyFields = body.getClass().getDeclaredFields();

        for (Field publishVoField : publishVoFields) {
            for (Field bodyField : bodyFields) {

                String publishVoFieldName = publishVoField.getName();
                String bodyFieldName = bodyField.getName();

                if (publishVoFieldName.equals(bodyFieldName)) {
                    try {
                        boolean isBodyFieldAccessible = bodyField.canAccess(body);
                        boolean isPublishVoFieldAccessible = publishVoField.canAccess(publishVo);
                        bodyField.setAccessible(true);
                        publishVoField.setAccessible(true);

                        Object bodyFieldValue = bodyField.get(body);
                        publishVoField.set(publishVo, bodyFieldValue);

                        bodyField.setAccessible(isBodyFieldAccessible);
                        publishVoField.setAccessible(isPublishVoFieldAccessible);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        return publishVo;
    }
}