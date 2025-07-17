package com.monglife.mongs.application.battle.port.aspect;

import com.monglife.mongs.application.battle.port.annotation.EndMatch;
import com.monglife.mongs.application.battle.port.exception.NotExistsMatchException;
import com.monglife.mongs.application.battle.port.exception.NotExistsMongException;
import com.monglife.mongs.application.battle.port.out.MongPersistencePort;
import com.monglife.mongs.domain.battle.model.Match;
import com.monglife.mongs.domain.battle.model.MatchPlayer;
import com.monglife.mongs.domain.mong.model.Mong;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class EndMatchAspect {

    private final MongPersistencePort mongPersistencePort;

    @AfterReturning(value = "@annotation(endMatch)", returning = "returnValue")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterReturning(JoinPoint joinPoint, EndMatch endMatch, Object returnValue) {
        if (returnValue instanceof Match match) {
            // 매치가 종료된 경우
            if (match.isEnd()) {
                // 승리한 매치 플레이어 조회
                MatchPlayer winMatchPlayer = match.getWinner();

                if (!winMatchPlayer.getIsBot()) {
                    // 승리한 매치 플레이어 몽 조회
                    Mong mong = mongPersistencePort.getMongPort(winMatchPlayer.getMongId())
                            .orElseThrow(NotExistsMongException::new);

                    // 매치 승리 보상 적용
                    mong.matchReward(Match.getRewardPayPoint(), Match.getRewardExp());

                    // 몽 동기화
                    mongPersistencePort.saveMongPort(mong);
                }
            }
        } else {
            throw new NotExistsMatchException();
        }
    }
}
