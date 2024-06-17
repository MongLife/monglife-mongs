package com.mongs.play.client.publisher.event.aspect;

import com.mongs.play.client.publisher.event.annotation.RealTimeMong;
import com.mongs.play.client.publisher.event.code.PublishEventCode;
import com.mongs.play.client.publisher.event.dto.BasicPublishEventDto;
import com.mongs.play.client.publisher.event.service.MqttEventService;
import com.mongs.play.client.publisher.event.vo.*;
import com.mongs.play.core.utils.ReflectionUtil;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(2)
@Aspect
@Component
@RequiredArgsConstructor
public class RealTimeMongAspect {

    private final MqttEventService mqttEventService;

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
            Long mongId = (Long) ReflectionUtil.getField(body, "mongId");
            List<Object> publishVoList = new ArrayList<>();

            for (PublishEventCode publishEventCode : annotation.codes()) {
                switch (publishEventCode) {
                    case MONG_CODE -> {
                        Object data = ReflectionUtil.setFields(body, new MongCodeVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_STATUS -> {
                        Object data = ReflectionUtil.setFields(body, new MongStatusVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_EXP -> {
                        Object data = ReflectionUtil.setFields(body, new MongExpVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_POOP_COUNT -> {
                        Object data = ReflectionUtil.setFields(body, new MongPoopCountVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_STATE -> {
                        Object data = ReflectionUtil.setFields(body, new MongStateVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_SHIFT -> {
                        Object data = ReflectionUtil.setFields(body, new MongShiftVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_PAY_POINT -> {
                        Object data = ReflectionUtil.setFields(body, new MongPayPointVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    case MONG_IS_SLEEPING -> {
                        Object data = ReflectionUtil.setFields(body, new MongIsSleepingVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    default -> {}
                }
            }

            mqttEventService.sendMong(mongId, publishVoList);
        }

        return returnValue;
    }
}