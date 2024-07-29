package com.mongs.play.client.publisher.event.aspect;

import com.mongs.play.client.publisher.event.annotation.RealTimeMember;
import com.mongs.play.client.publisher.event.code.PublishEventCode;
import com.mongs.play.client.publisher.event.dto.BasicPublishEventDto;
import com.mongs.play.client.publisher.event.event.RealTimeMemberEvent;
import com.mongs.play.client.publisher.event.event.RealTimeMongEvent;
import com.mongs.play.client.publisher.event.service.MqttEventService;
import com.mongs.play.client.publisher.event.vo.MemberStarPointVo;
import com.mongs.play.core.utils.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class RealTimeMemberAspect {

//    private final MqttEventService mqttEventService;
    private final ApplicationEventPublisher publisher;

    @Pointcut("execution(* com.mongs.play..*.*(..))")
    public void cut() {}

    @Around(value = "cut() && @annotation(annotation)")
    public Object realTimeMember(ProceedingJoinPoint joinPoint, RealTimeMember annotation) throws Throwable {
        Object returnValue = joinPoint.proceed();

        Object body = returnValue;

        if (returnValue instanceof ResponseEntity<?>) {
            body = ((ResponseEntity<?>) returnValue).getBody();
        }

        if (!ObjectUtils.isEmpty(body)) {

            Long accountId = (Long) ReflectionUtil.getField(body, "accountId");
            List<Object> publishVoList = new ArrayList<>();

            for (PublishEventCode publishEventCode : annotation.codes()) {
                switch (publishEventCode) {
                    case MEMBER_STAR_POINT -> {
                        Object data = ReflectionUtil.setFields(body, new MemberStarPointVo());
                        publishVoList.add(BasicPublishEventDto.builder().code(publishEventCode).data(data).build());
                    }
                    default -> {}
                }
            }

//            mqttEventService.sendMember(accountId, publishVoList);
            publisher.publishEvent(RealTimeMemberEvent.builder()
                    .accountId(accountId)
                    .publishVoList(publishVoList)
                    .build());
        }

        return returnValue;
    }
}