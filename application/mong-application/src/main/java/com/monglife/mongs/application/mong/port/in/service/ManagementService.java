package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.enums.MongSchedulerTypeCode;
import com.monglife.mongs.application.mong.port.exception.InvalidCreateMongException;
import com.monglife.mongs.application.mong.port.exception.InvalidStrokeMongException;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.in.ManagementUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.MongPublishPort;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.enums.MongStateCode;
import com.monglife.mongs.domain.enums.MongStatusCode;
import com.monglife.mongs.domain.model.Mong;
import com.monglife.mongs.domain.model.MongType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ManagementService implements ManagementUseCase {

    private static final Random random = new Random();

    private final MongSchedulerPort mongSchedulerPort;

    private final MongPersistencePort mongPersistencePort;

    private final MongEventPort mongEventPort;

    private final MongPublishPort mongPublishPort;

    /**
     * 몽 생성
     */
    @Override
    @Transactional
    public void createMongUseCase(CreateMongCommand command) {
        // 0 레벨의 몽 타입 목록 조회
        List<MongType> mongTypeCodes = mongPersistencePort.getMongTypes(0);

        // 몽 타입이 없는 경우 예외
        if (mongTypeCodes.isEmpty()) {
            throw new InvalidCreateMongException();
        }

        // 몽 타입 랜덤 배정
        MongType mongType = mongTypeCodes.get(random.nextInt(0, mongTypeCodes.size()));

        // 새로운 몽 영속화
        Mong mong = mongPersistencePort.createMongPort(CreateMongVo.builder()
                .accountId(command.getAccountId())
                .mongName(command.getMongName())
                .mongTypeCode(mongType.getMongTypeCode())
                .statusCode(MongStatusCode.NORMAL)
                .stateCode(MongStateCode.NORMAL)
                .level(mongType.getLevel())
                .maxStatus(mongType.getMaxStatus())
                .sleepAt(command.getSleepAt())
                .wakeupAt(command.getWakeupAt())
                .payPoint(0)
                .isSleep(false)
                .strength(mongType.getMaxStatus())
                .satiety(mongType.getMaxStatus())
                .healthy(mongType.getMaxStatus())
                .fatigue(mongType.getMaxStatus())
                .exp(mongType.getMaxStatus())
                .weight(0D)
                .poopCount(0)
                .canRandomDrawCount(0)
                .build())
                .orElseThrow(InvalidCreateMongException::new);

        // 몽 1차 진화 스케줄 등록
        mongSchedulerPort.createTaskPort(mong.getMongId(), MongSchedulerTypeCode.EGG_EVOLUTION);

        // 몽 생성 이벤트 발생
        mongEventPort.createMongEventPort(command.getAccountId(), mongType.getMongTypeCode());
    }

    /**
     * 몽 삭제
     */
    @Override
    @Transactional
    public void deleteMongUseCase(DeleteMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 삭제
        mongPersistencePort.deleteMongPort(mong);

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());
    }

    /**
     * 몽 사망
     */
    @Override
    @Transactional
    public void deadMongUseCase(DeadMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 사망
        mong.dead();

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 목록 조회
     */
    @Override
    @Transactional
    public List<Mong> getMongsUseCase(GetMongsCommand command) {
        return mongPersistencePort.getMongsPort(command.getAccountId());
    }

    /**
     * 몽 조회
     */
    @Override
    @Transactional
    public Mong getMongUseCase(GetMongCommand command) {
        return mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());
    }

    /**
     * 몽 쓰다 듬기
     */
    @Override
    @Transactional
    public Mong strokeMongUseCase(StrokeMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 쓰다 듬기 대기 시간 조회
        Long expirationSeconds = mongPersistencePort.getMongStrokeExpirationSecondsPort(command.getMongId());

        // 쓰다 듬기 대기 시간이 남은 경우 예외
        if (expirationSeconds > 0) {
            throw new InvalidStrokeMongException(expirationSeconds);
        }

        // 몽 쓰다 듬기 이력 등록
        mongPersistencePort.createMongStrokeHistoryPort(mong.getMongId(), mong.getStrokeExpirationSeconds());

        // 몽 쓰다 듬기
        mong.stroke();

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 수면
     */
    @Override
    @Transactional
    public Mong sleepMongUseCase(SleepMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        if (Boolean.TRUE.equals(mong.getIsSleep())) {
            mong.wakeup();
            mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerTypeCode.INCREASE_STATUS);
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.DECREASE_STATUS);
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.INCREASE_POOP);
        } else {
            mong.sleep();
            mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerTypeCode.DECREASE_STATUS);
            mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerTypeCode.INCREASE_POOP);
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.INCREASE_STATUS);
        }

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 배변 처리
     */
    @Override
    @Transactional
    public Mong poopCleanMongUseCase(PoopCleanMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 배변 처리
        mong.poopClean();

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 진화
     */
    @Override
    @Transactional
    public Mong evolutionMongUseCase(EvolutionMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 진화 점수 조회
        Double evolutionScore = mong.getEvolutionScore();

        // 진화 가능한 몽 타입 목록 조회
        List<MongType> mongTypeCodes = mongPersistencePort.getMongTypes(evolutionScore, mong.getMongTypeCode());

        // 몽 진화
        mong.evolution(mongTypeCodes);

        // 첫 진화인 경우 스케줄 등록
        if (1 == mong.getLevel()) {
            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.SLEEP, mong.getSleepAt());
            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.WAKEUP, mong.getWakeupAt());
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.DECREASE_STATUS);
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), MongSchedulerTypeCode.INCREASE_STATUS);
        }

        // 몽 진화 이벤트 발생
        mongEventPort.evolutionMongEventPort(command.getAccountId(), mong.getMongTypeCode());

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 졸업
     */
    @Override
    @Transactional
    public Mong graduateMongUseCase(GraduateMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 졸업
        mong.graduate();

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 페이 포인트 증가
     */
    @Override
    @Transactional
    public Mong increaseMongPayPointUseCase(IncreaseMongPayPointCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 페이 포인트 증가
        mong.increasePayPoint(command.getPayPoint());

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 지수 증가
     */
    @Override
    @Transactional
    public Mong increaseMongStatusUseCase(IncreaseMongStatusCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 지수 증가
        mong.cycleIncreaseStatus();

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 지수 감소
     */
    @Override
    @Transactional
    public Mong decreaseMongStatusUseCase(DecreaseMongStatusCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 지수 감소
        mong.cycleDecreaseStatus();

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }

    /**
     * 몽 배변 수 증가
     */
    @Override
    @Transactional
    public Mong increaseMongPoopCountUseCase(IncreaseMongPoopCountCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 배변 수 증가
        mong.cycleIncreasePoopCount();

        // 몽 정보 비동기 응답
        mongPublishPort.publishMongPort(mong);

        return mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);
    }
}
