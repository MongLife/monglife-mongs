package com.mongs.management.global.event;

import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.management.global.entity.Mong;
import com.mongs.management.domain.management.service.vo.MongVo;
import com.mongs.management.global.moduleService.vo.PublishEvolutionReadyVo;
import com.mongs.management.global.moduleService.vo.PublishStateVo;
import com.mongs.management.global.event.vo.EvolutionCheckEvent;
import com.mongs.management.global.event.vo.StateCheckEvent;
import com.mongs.management.global.moduleService.MongHistoryService;
import com.mongs.management.global.moduleService.MongService;
import com.mongs.management.global.moduleService.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.mongs.core.utils.MongUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceEventListener {
    private final MongService mongService;
    private final MongHistoryService mongHistoryService;
    private final NotificationService notificationService;

    /**
     * 진화 가능 여부 체크
     * 진화가 가능한지 여부를 확인하여 진화 준비 상태로 변경한다.
     *
     * @param event 진화 체크 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evolutionCheckEventListener(EvolutionCheckEvent event) {
        Mong mong = event.mong();

        log.info("[{}] evolutionCheckEventListener", mong.getId());

        if (isEvolutionReady(mong.getExp(), mong.getGrade())) {
            Mong saveMong = mongService.saveMong(mong.toBuilder()
                    .shift(MongShift.EVOLUTION_READY)
                    .build());

            MongVo mongVo = MongVo.of(saveMong);

            notificationService.publishEvolutionReady(saveMong.getAccountId(), PublishEvolutionReadyVo.of(mongVo));
        }
    }

    /**
     * 상태 변경 여부 체크
     * 몽 지수를 확인하여 상태를 변경시킨다.
     *
     * @param event 상태 체크 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stateCheckEventListener(StateCheckEvent event) {
        Mong mong = event.mong();

        log.info("[{}] stateCheckEventListener", mong.getId());

        MongState nextState = getNextState(
                mong.getState(),
                mong.getGrade(),
                mong.getWeight(),
                mong.getStrength(),
                mong.getSatiety(),
                mong.getHealthy(),
                mong.getSleep()
        );

        Mong saveMong = mongService.saveMong(mong.toBuilder()
                .state(nextState)
                .build());

        MongVo mongVo = MongVo.of(saveMong);

        notificationService.publishState(saveMong.getAccountId(), PublishStateVo.of(mongVo));
    }

}
