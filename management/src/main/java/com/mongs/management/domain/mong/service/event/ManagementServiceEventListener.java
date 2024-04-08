package com.mongs.management.domain.mong.service.event;

import com.mongs.core.enums.management.MongHistoryCode;
import com.mongs.core.enums.management.MongShift;
import com.mongs.core.enums.management.MongState;
import com.mongs.core.vo.mqtt.*;
import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.domain.mong.service.componentService.vo.MongVo;
import com.mongs.management.domain.mong.service.event.vo.*;
import com.mongs.management.domain.mong.service.moduleService.MongHistoryService;
import com.mongs.management.domain.mong.service.moduleService.MongService;
import com.mongs.management.domain.mong.service.moduleService.NotificationService;
import com.mongs.management.domain.mong.service.moduleService.vo.PublishCreateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.mongs.core.utils.MongStatusUtil.statusToPercent;
import static com.mongs.core.utils.MongUtil.*;

@Component
@RequiredArgsConstructor
public class ManagementServiceEventListener {
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

    /**
     * 
     * @param event 몽 생성 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerMongEventListener(RegisterMongEvent event) {
        MongVo mongVo = event.mongVo();

        /* 몽 생성 알림 전송 */
        notificationService.publishCreate(mongVo.accountId(), PublishCreateVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.CREATE);
    }

    /**
     * 
     * @param event 몽 삭제 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteMongEventListener(DeleteMongEvent event) {
        MongVo mongVo = event.mongVo();

        notificationService.publishDelete(mongVo.accountId(), PublishDeleteVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.DELETE);

    }

    /**
     * 
     * @param event 몽 쓰다듬기 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void strokeMongEventListener(StrokeMongEvent event) {
        MongVo mongVo = event.mongVo();

        notificationService.publishStroke(mongVo.accountId(), PublishStrokeVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.STROKE);
    }

    /**
     *
     * @param event 몽 식사 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void feedMongEventListener(FeedMongEvent event) {
        MongVo mongVo = event.mongVo();

        notificationService.publishFeed(mongVo.accountId(), PublishFeedVo.of(mongVo));

        mongHistoryService.saveMongHistory(mongVo.mongId(), MongHistoryCode.FEED);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sleepingMongEventListener(SleepingMongEvent event) {

    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void poopCleanEventListener(PoopCleanEvent event) {

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void trainingMongEventListener(TrainingMongEvent event) {

    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void graduateMongEventListener(GraduateMongEvent event) {

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evolutionMongEventListener(EvolutionMongEvent event) {

    }
}
