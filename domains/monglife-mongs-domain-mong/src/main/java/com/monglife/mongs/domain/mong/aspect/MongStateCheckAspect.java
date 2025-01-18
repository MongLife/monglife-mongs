package com.monglife.mongs.domain.mong.aspect;

import com.monglife.mongs.domain.mong.annotation.AllowMongState;
import com.monglife.mongs.domain.mong.annotation.DenyMongState;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
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
public class MongStateCheckAspect {

    private final MongService mongService;

    @Pointcut("@annotation(com.monglife.mongs.domain.mong.annotation.DenyMongState)")
    private void denyMongStatePointcut() {}

    @Pointcut("@annotation(com.monglife.mongs.domain.mong.annotation.AllowMongState)")
    private void allowMongStatePointcut() {}

    /**
     * 상태 변경 불가능 여부 확인
     */
    @Before(value = "denyMongStatePointcut() && @annotation(denyMongState)")
    public void before(JoinPoint joinPoint, DenyMongState denyMongState) {

        boolean egg = denyMongState.egg();
        boolean sleep = denyMongState.sleep();
        List<MongStateCode> mongStateCodes = Arrays.stream(denyMongState.stateCodes()).toList();
        String mongIdParameterName = denyMongState.mongIdFieldName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        Long mongId = (Long) this.getParameterValue(mongIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(mongIdParameterName));

        MongVo mongVo = mongService.getMong(mongId);

        // 기상 상태 여부 확인
        if (sleep && mongVo.getIsSleep()) {
            throw new InvalidMongStateException();
        }
        // 알 여부 확인 (알인 경우에만 실행 가능)
        else if (egg && mongVo.getIsEgg()) {
            throw new InvalidMongTypeLevelException(mongVo.getMongId(), mongVo.getMongTypeCode(), mongVo.getLevel());
        }
        // 몽 상태 여부 확인
        else if(mongStateCodes.contains(mongVo.getStateCode())) {
            throw new InvalidMongStateException();
        }
    }

    /**
     * 상태 변경 가능 여부 확인
     */
    @Before(value = "allowMongStatePointcut() && @annotation(allowMongState)")
    public void before(JoinPoint joinPoint, AllowMongState allowMongState) {

        List<MongStateCode> mongStateCodes = Arrays.stream(allowMongState.stateCodes()).toList();
        String mongIdParameterName = allowMongState.mongIdFieldName();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        Long mongId = (Long) this.getParameterValue(mongIdParameterName, parameters, args)
                .orElseThrow(() -> new NotExistsParameterException(mongIdParameterName));

        MongVo mongVo = mongService.getMong(mongId);

        // 몽 상태 여부 확인
        if(!mongStateCodes.contains(mongVo.getStateCode())) {
            throw new InvalidMongStateException();
        }
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
