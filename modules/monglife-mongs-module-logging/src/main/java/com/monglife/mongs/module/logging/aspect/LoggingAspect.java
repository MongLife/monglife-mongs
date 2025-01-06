package com.monglife.mongs.module.logging.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    public LoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Pointcut("execution(* com.monglife.mongs..*Consumer.*(..))")
    private void consumerPointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Controller.*(..))")
    private void controllerPointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Service.*(..))")
    private void servicePointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Listener.*(..))")
    private void listenerPointcut() {}

    @Pointcut("execution(* com.monglife.mongs..*Repository.*(..))")
    private void repositoryPointcut() {}

    @Pointcut("consumerPointcut() || controllerPointcut() || servicePointcut() || listenerPointcut()")
    private void targetPointcut() {}

    @Before("targetPointcut() && !@annotation(org.springframework.transaction.annotation.Transactional)")
    public void around(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();

        log.info("[INVOKE] [-] {}#{} {}", clazzName, methodName, generateArgs(method, joinPoint.getArgs()));
    }

    @Before("targetPointcut() && @annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransactional(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();

        log.info("[INVOKE] [{}] {}#{} {}", TransactionSynchronizationManager.getCurrentTransactionName(), clazzName, methodName, generateArgs(method, joinPoint.getArgs()));

    }

    @AfterThrowing(value = "controllerPointcut() || servicePointcut() || listenerPointcut()", throwing = "exception")
    public void afterThrowingException(JoinPoint joinPoint, Exception exception) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String clazzName = method.getDeclaringClass().getName();
        String methodName = method.getName();

        String message = exception.getMessage();

        if (exception instanceof ErrorException errorException) {
            message = errorException.getResponse().getMessage();
        }

        log.error("[THROW] {}#{}\n{}", clazzName, methodName, message);
    }

    private String generateArgs(Method method, Object[] args) {

        StringBuilder argsBuilder = new StringBuilder();
        Parameter[] parameters = method.getParameters();
        for (int index = 0; index < parameters.length; index++) {
            if (args[index] == null) {
                argsBuilder.append("null");
            } else if (args[index].getClass().isPrimitive()) {
                argsBuilder
                        .append("\n")
                        .append("[")
                        .append(index)
                        .append("] ")
                        .append(parameters[index].getName())
                        .append("<")
                        .append(args[index].getClass().getTypeName())
                        .append("> : ")
                        .append(args[index]);
            } else {
                try {
                    String argJson = objectMapper.writeValueAsString(args[index]);
                    argsBuilder
                            .append("\n")
                            .append("[")
                            .append(index)
                            .append("] ")
                            .append(parameters[index].getName())
                            .append("<")
                            .append(args[index].getClass().getTypeName())
                            .append("> : ")
                            .append(argJson);
                } catch (JsonProcessingException ignored) {
                    argsBuilder
                            .append("\n")
                            .append("[")
                            .append(index)
                            .append("] ")
                            .append(parameters[index].getName())
                            .append("<")
                            .append(args[index].getClass().getTypeName())
                            .append("> : ")
                            .append(args[index].toString());
                }
            }

            if (index != parameters.length - 1) argsBuilder.append(", ");
        }

        return argsBuilder.toString();
    }
}
