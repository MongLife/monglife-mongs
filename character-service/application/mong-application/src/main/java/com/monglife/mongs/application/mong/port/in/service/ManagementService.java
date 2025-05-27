package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.annotation.CheckMongDead;
import com.monglife.mongs.application.mong.port.annotation.PublishMongPort;
import com.monglife.mongs.application.mong.port.enums.MongSchedulerType;
import com.monglife.mongs.application.mong.port.exception.*;
import com.monglife.mongs.application.mong.port.in.ManagementUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.MongReadPort;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import com.monglife.mongs.application.mong.port.out.vo.CreateMongVo;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import com.monglife.mongs.domain.mong.enums.MongStatusCode;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.MongType;
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

    private final MongReadPort mongReadPort;

    private final MongEventPort mongEventPort;

    /**
     * 몽 생성
     */
    @Override
    @Transactional
    public Mong createMongUseCase(CreateMongCommand command) {
        // 0 레벨의 몽 타입 목록 조회
        List<MongType> mongTypes = mongReadPort.getMongTypesPort(0);

        // 몽 타입이 없는 경우 예외
        if (mongTypes.isEmpty()) {
            throw new InvalidCreateMongException();
        }

        // 몽 타입 랜덤 배정
        MongType mongType = mongTypes.get(random.nextInt(0, mongTypes.size()));

        // 새로운 몽 영속화
        Mong mong = mongPersistencePort.createMongPort(CreateMongVo.builder()
                .accountId(command.getAccountId())
                .mongName(command.getMongName())
                .statusCode(MongStatusCode.NORMAL)
                .stateCode(MongStateCode.NORMAL)
                .sleepAt(command.getSleepAt())
                .wakeupAt(command.getWakeupAt())
                .payPoint(0)
                .isSleep(false)
                .strength(mongType.getMaxStatus())
                .satiety(mongType.getMaxStatus())
                .healthy(mongType.getMaxStatus())
                .fatigue(mongType.getMaxStatus())
                .exp(0D)
                .weight(0D)
                .evolutionReward(0D)
                .evolutionPenalty(0D)
                .strokeCount(0)
                .trainingCount(0)
                .poopCount(0)
                .randomDrawTicketCount(0)
                .mongType(mongType)
                .build())
                .orElseThrow(InvalidCreateMongException::new);

        // 알 부화 스케줄 등록
        mongSchedulerPort.createTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.EGG_EVOLUTION)
                .orElseThrow(InvalidCreateMongScheduleException::new);

        // 몽 생성 이벤트 발생
        mongEventPort.createMongEventPort(command.getAccountId(), mongType.getMongTypeCode());

        return mong;
    }

    /**
     * 몽 삭제
     */
    @Override
    @Transactional
    public Mong deleteMongUseCase(DeleteMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 삭제
        mong = mongPersistencePort.deleteMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());

        return mong;
    }

    /**
     * 몽 사망
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong deadMongUseCase(DeadMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 사망
        mong.dead();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());

        return mong;
    }

    /**
     * 몽 목록 조회
     */
    @Override
    @Transactional
    public List<Mong> getMongsUseCase(GetMongsCommand command) {
        return mongReadPort.getMongsPort(command.getAccountId());
    }

    /**
     * 몽 조회
     */
    @Override
    @Transactional
    public Mong getMongUseCase(GetMongCommand command) {
        return mongReadPort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());
    }

    /**
     * 몽 쓰다 듬기
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong strokeMongUseCase(StrokeMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 쓰다 듬기 대기 시간 조회
        Long expirationSeconds = mongReadPort.getMongStrokeExpirationSecondsPort(command.getMongId());

        // 쓰다 듬기 대기 시간이 남은 경우 예외
        if (expirationSeconds > 0) {
            throw new InvalidStrokeMongException(expirationSeconds);
        }

        // 몽 쓰다 듬기
        mong.stroke();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 몽 쓰다 듬기 이력 등록
        mongPersistencePort.createMongStrokeHistoryPort(mong.getMongId(), Mong.getStrokeExpirationSeconds())
                .orElseThrow(InvalidCreateMongStrokeHistoryException::new);

        return mong;
    }

    /**
     * 몽 수면
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong sleepMongUseCase(SleepMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 수면
        mong.sleep();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 스케줄 등록 및 삭제
        mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerType.DECREASE_STATUS);
        mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerType.INCREASE_POOP);
        mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.INCREASE_STATUS)
                .orElseThrow(InvalidCreateMongScheduleException::new);

        return mong;
    }

    /**
     * 몽 기상
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong wakeUpMongUseCase(WakeupMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 기상
        mong.wakeup();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 스케줄 등록 및 삭제
        mongSchedulerPort.deleteTaskPort(mong.getMongId(), MongSchedulerType.INCREASE_STATUS);
        mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.DECREASE_STATUS)
                .orElseThrow(InvalidCreateMongScheduleException::new);
        mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.INCREASE_POOP)
                .orElseThrow(InvalidCreateMongScheduleException::new);

        return mong;
    }

    /**
     * 몽 배변 처리
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong poopCleanMongUseCase(PoopCleanMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 배변 처리
        mong.poopClean();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 몽 진화 준비
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong evolutionReadyMongUseCase(EvolutionReadyMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        mong.evolutionReady();

        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 몽 진화
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong evolutionMongUseCase(EvolutionMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 진화 점수 조회
        Double evolutionScore = mong.getEvolutionScore();

        // 진화 가능한 몽 타입 목록 조회
        List<MongType> mongTypeCodes = mongReadPort.getNextLevelMongTypesPort(evolutionScore, mong.getMongTypeCode());

        // 몽 진화
        mong.evolution(mongTypeCodes);

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 알 진화 경우 스케줄 등록
        if (mong.getLevel() == 1) {
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.DECREASE_STATUS)
                    .orElseThrow(InvalidCreateMongScheduleException::new);
            mongSchedulerPort.createCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.INCREASE_POOP)
                    .orElseThrow(InvalidCreateMongScheduleException::new);
            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.SLEEP, mong.getSleepAt())
                    .orElseThrow(InvalidCreateMongScheduleException::new);
            mongSchedulerPort.createFixedTimeCycleTaskPort(mong.getMongId(), mong.getAccountId(), MongSchedulerType.WAKEUP, mong.getWakeupAt())
                    .orElseThrow(InvalidCreateMongScheduleException::new);
        }

        // 몽 진화 이벤트 발생
        mongEventPort.evolutionMongEventPort(command.getAccountId(), mong.getMongTypeCode());

        return mong;
    }

    /**
     * 몽 졸업
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong graduateMongUseCase(GraduateMongCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 졸업
        mong.graduate();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 모든 스케줄 삭제
        mongSchedulerPort.deleteAllTaskPort(mong.getMongId());

        return mong;
    }

    /**
     * 몽 페이 포인트 증가
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong increaseMongPayPointUseCase(IncreaseMongPayPointCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new);

        // 몽 페이 포인트 증가
        mong.increasePayPoint(command.getPayPoint());

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 몽 지수 증가
     */
    @Override
    @Transactional
    @CheckMongDead
    @PublishMongPort
    public Mong increaseMongStatusUseCase(IncreaseMongStatusCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 지수 증가
        mong.cycleIncreaseStatus();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 몽 지수 감소
     */
    @Override
    @Transactional
    @CheckMongDead
    @PublishMongPort
    public Mong decreaseMongStatusUseCase(DecreaseMongStatusCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 지수 감소
        mong.cycleDecreaseStatus();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 몽 배변 수 증가
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong increaseMongPoopCountUseCase(IncreaseMongPoopCountCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 몽 배변 수 증가
        mong.cycleIncreasePoopCount();

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }
}
