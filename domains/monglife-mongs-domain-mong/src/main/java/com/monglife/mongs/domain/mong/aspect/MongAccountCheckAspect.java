package com.monglife.mongs.domain.mong.aspect;

import com.monglife.mongs.domain.mong.annotation.DenyMongState;
import com.monglife.mongs.domain.mong.annotation.VerifyMongAccount;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.exception.InvalidMongException;
import com.monglife.mongs.domain.mong.exception.InvalidMongStateException;
import com.monglife.mongs.domain.mong.exception.InvalidMongTypeLevelException;
import com.monglife.mongs.domain.mong.exception.NotExistsParameterException;
import com.monglife.mongs.domain.mong.service.MongService;
import com.monglife.mongs.domain.mong.vo.MongVo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class MongAccountCheckAspect {

    private final MongService mongService;

    @Pointcut("@annotation(com.monglife.mongs.domain.mong.annotation.VerifyMongAccount)")
    private void verifyMongAccountPointcut() {}

    /**
     * 계정 소유의 몽인지 판별 Aspect
     */
    @Before(value = "verifyMongAccountPointcut() && @annotation(verifyMongAccount)")
    public void before(JoinPoint joinPoint, VerifyMongAccount verifyMongAccount) {

        String accountIdParameterName = verifyMongAccount.accountIdFieldName();
        String mongIdParameterName = verifyMongAccount.mongIdFieldName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        Long accountId = (Long) this.getParameterValue(accountIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(accountIdParameterName));

        Long mongId = (Long) this.getParameterValue(mongIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(mongIdParameterName));

        mongService.getMong(accountId, mongId)
                .orElseThrow(() -> new InvalidMongException(accountId, mongId));
    }

    /**
     * 파라미터명을 기준으로 파라미터 값 반환 함수
     * @param parameterName 파라미터명
     * @param parameters 파라미터 목록
     * @param args 파라미터 값 목록
     * @return 매칭되는 파라미터 값
     */
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
