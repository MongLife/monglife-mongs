package com.mongs.play.client.publisher.event.aspect;

import com.mongs.play.client.publisher.event.annotation.RealTimeMong;
import com.mongs.play.client.publisher.event.code.PublishCode;
import com.mongs.play.client.publisher.event.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.event.service.MqttService;
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
            Long mongId = (Long) ReflectionUtil.getField(body, "mongId");
            List<Object> publishVoList = new ArrayList<>();

            for (PublishCode publishCode : annotation.codes()) {
                switch (publishCode) {
                    case MONG_CODE -> {
                        Object data = ReflectionUtil.setFields(body, new MongCodeVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_STATUS -> {
                        Object data = ReflectionUtil.setFields(body, new MongStatusVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_EXP -> {
                        Object data = ReflectionUtil.setFields(body, new MongExpVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_POOP_COUNT -> {
                        Object data = ReflectionUtil.setFields(body, new MongPoopCountVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_STATE -> {
                        Object data = ReflectionUtil.setFields(body, new MongStateVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_SHIFT -> {
                        Object data = ReflectionUtil.setFields(body, new MongShiftVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_PAY_POINT -> {
                        Object data = ReflectionUtil.setFields(body, new MongPayPointVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    case MONG_IS_SLEEPING -> {
                        Object data = ReflectionUtil.setFields(body, new MongIsSleepingVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    default -> {}
                }
            }

            mqttService.sendMong(mongId, publishVoList);
        }

        return returnValue;
    }
}