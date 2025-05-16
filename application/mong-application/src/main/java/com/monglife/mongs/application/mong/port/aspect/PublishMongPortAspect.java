package com.monglife.mongs.application.mong.port.aspect;

import com.monglife.mongs.application.mong.port.annotation.PublishMongPort;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PublishMongPortAspect {

    private final MongPublishPort mongPublishPort;

    @AfterReturning(value = "@annotation(publishMongPort)", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, PublishMongPort publishMongPort, Object returnValue) {
        if (returnValue instanceof Mong mong) {
            mongPublishPort.publishMongPort(mong);
        } else {
            throw new NotExistsMongException();
        }
    }
}
