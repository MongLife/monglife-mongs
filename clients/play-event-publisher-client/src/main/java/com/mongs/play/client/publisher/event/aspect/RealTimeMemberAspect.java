package com.mongs.play.client.publisher.event.aspect;

import com.mongs.play.client.publisher.event.annotation.RealTimeMember;
import com.mongs.play.client.publisher.event.code.PublishCode;
import com.mongs.play.client.publisher.event.dto.res.BasicPublishDto;
import com.mongs.play.client.publisher.event.service.MqttService;
import com.mongs.play.client.publisher.event.vo.MemberStarPointVo;
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
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class RealTimeMemberAspect {

    private final MqttService mqttService;

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

            for (PublishCode publishCode : annotation.codes()) {
                switch (publishCode) {
                    case MEMBER_STAR_POINT -> {
                        Object data = ReflectionUtil.setFields(body, new MemberStarPointVo());
                        publishVoList.add(BasicPublishDto.builder().code(publishCode).data(data).build());
                    }
                    default -> {}
                }
            }

            mqttService.sendMember(accountId, publishVoList);
        }

        return returnValue;
    }
}