package com.monglife.mongs.app.manager.global.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.core.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.monglife.mongs..*Controller.*(..))")
    private void controllerPointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Service.*(..))")
    private void servicePointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Listener.*(..))")
    private void listenerPointcut() {}

    @Before("controllerPointcut() || servicePointcut() || listenerPointcut()")
    public void before(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();

        StringBuilder argsBuilder = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        for (int index = 0; index < parameters.length; index++) {
            if (args[index].getClass().isPrimitive()) {
                argsBuilder
                        .append(parameters[index].getName())
                        .append("(")
                        .append(args[index].getClass().getTypeName())
                        .append(") : ")
                        .append(args[index]);
            } else {
                try {
                    String argJson = objectMapper.writeValueAsString(args[index]);
                    argsBuilder
                            .append(parameters[index].getName())
                            .append("(")
                            .append(args[index].getClass().getTypeName())
                            .append(") : ")
                            .append(argJson);
                } catch (JsonProcessingException ignored) {}
            }

            if (index != parameters.length - 1) argsBuilder.append(", ");
        }

        log.debug("[Method Call] {}#{} =====> {}", clazzName, methodName, argsBuilder);
    }

    @AfterThrowing(value = "controllerPointcut() || servicePointcut() || listenerPointcut()", throwing = "exception")
    public void afterThrowingException(JoinPoint joinPoint, Exception exception) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();

        String message = exception.getMessage();
        if (exception instanceof ErrorException errorException) message = errorException.getResponse().getMessage();
        log.error("[Throwing] {}#{} =====> {}", clazzName, methodName, message);
    }
}
