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
            // 몽 변동 비동기 응답 전송
            mongPublishPort.publishMongPort(mong);

            // 몽 상태 코드 변동 알림 전송
            if (mong.getIsMongStateChange()) {
                String title = "";
                String body = "";

                switch (mong.getStateCode()) {
                    case DEAD -> {
                        title = "죽은 몽이 있어요";
                        body = mong.getMongName() + "(이)가 죽었어요...";
                    }

                    case EVOLUTION_READY -> {
                        title = "진화 준비가 되었어요";
                        body = mong.getMongName() + "(을)를 새로운 몽으로 진화시켜 주세요";
                    }

                    case GRADUATE_READY -> {
                        title = "졸업 준비가 되었어요";
                        body = mong.getMongName() + "(을)를 졸업 시켜 주세요";
                    }
                }

                // 알림 전송
                mongPublishPort.publishMongPort(mong.getAccountId(), title, body);
            }

            // 몽 지수 코드 변동 알림 전송
            if (mong.getIsMongStatusCodeChange()) {
                String title = "";
                String body = "";

                switch (mong.getStatusCode()) {
                    case SOMNOLENCE -> {
                        title = "졸린 몽이 있어요";
                        body = mong.getMongName() + "(을)를 재워야 해요";
                    }
                    case HUNGRY -> {
                        title = "배고픈 몽이 있어요";
                        body = mong.getMongName() + "에게 밥을 줘야 해요";

                    }
                    case SICK -> {
                        title = "아픈 몽이 있어요";
                        body = mong.getMongName() + "의 체력을 채워야 해요";
                    }
                }

                // 알림 전송
                mongPublishPort.publishMongPort(mong.getAccountId(), title, body);
            }

        } else {
            throw new NotExistsMongException();
        }
    }
}
