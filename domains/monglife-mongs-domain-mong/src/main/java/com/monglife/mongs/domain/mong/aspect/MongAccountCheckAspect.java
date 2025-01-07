package com.monglife.mongs.domain.mong.aspect;

import com.monglife.mongs.domain.mong.annotation.MongAccountCheck;
import com.monglife.mongs.domain.mong.exception.InvalidMongException;
import com.monglife.mongs.domain.mong.exception.NotExistsParameterException;
import com.monglife.mongs.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class MongAccountCheckAspect {

    private final MongService mongService;


    @Pointcut("@annotation(com.monglife.mongs.domain.mong.annotation.MongAccountCheck)")
    private void mongAccountCheckPointcut() {}

    @Before(value = "mongAccountCheckPointcut() && @annotation(mongAccountCheck)")
    public void before(JoinPoint joinPoint, MongAccountCheck mongAccountCheck) {

        String accountIdParameterName = mongAccountCheck.accountId();
        String mongIdParameterName = mongAccountCheck.mongId();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        Long accountId = (Long) this.getParameterValue(accountIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(accountIdParameterName));

        Long mongId = (Long) this.getParameterValue(mongIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(mongIdParameterName));

        if (!mongService.validateMongByAccountId(accountId, mongId)) {
            throw new InvalidMongException(accountId, mongId);
        }
    }

    private Optional<Object> getParameterValue(String parameterName, Parameter[] parameters, Object[] args) {

        Object arg = null;

        for (int index = 0; index < parameters.length; index++) {
            if (parameters[index].getName().equals(parameterName)) {
                arg = args[index];
            }
        }

        return Optional.ofNullable(arg);
    }
}
