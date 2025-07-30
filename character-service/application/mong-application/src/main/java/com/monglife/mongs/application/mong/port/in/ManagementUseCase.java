package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.Mong;

import java.util.List;

public interface ManagementUseCase {

    /**
     * 몽 생성
     */
    Mong createMongUseCase(CreateMongCommand command);

    /**
     * 몽 삭제
     */
    Mong deleteMongUseCase(DeleteMongCommand command);

    /**
     * 몽 사망
     */
    Mong deadMongUseCase(DeadMongCommand command);

    /**
     * 몽 목록 조회
     */
    List<Mong> getMongsUseCase(GetMongsCommand command);

    /**
     * 몽 조회
     */
    Mong getMongUseCase(GetMongCommand command);

    /**
     * 몽 쓰다 듬기
     */
    Mong strokeMongUseCase(StrokeMongCommand command);

    /**
     * 몽 수면
     */
    Mong sleepMongUseCase(SleepMongCommand command);

    /**
     * 몽 기상
     */
    Mong wakeUpMongUseCase(WakeupMongCommand command);

    /**
     * 몽 배변 처리
     */
    Mong poopCleanMongUseCase(PoopCleanMongCommand command);

    /**
     * 몽 진화 준비
     */
    Mong evolutionReadyMongUseCase(EvolutionReadyMongCommand command);

    /**
     * 몽 진화
     */
    Mong evolutionMongUseCase(EvolutionMongCommand command);

    /**
     * 몽 졸업
     */
    Mong graduateMongUseCase(GraduateMongCommand command);

    /**
     * 몽 페이 포인트 증가
     */
    Mong increaseMongPayPointUseCase(IncreaseMongPayPointCommand command);

    /**
     * 몽 지수 증가
     */
    Mong increaseMongStatusUseCase(IncreaseMongStatusCommand command);

    /**
     * 몽 지수 감소
     */
    Mong decreaseMongStatusUseCase(DecreaseMongStatusCommand command);

    /**
     * 몽 배변 수 증가
     */
    Mong increaseMongPoopCountUseCase(IncreaseMongPoopCountCommand command);
}
