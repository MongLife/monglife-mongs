package com.mongs.lifecycle.service.event;

import com.mongs.core.enums.management.MongState;
import com.mongs.lifecycle.entity.Mong;
import com.mongs.lifecycle.repository.MongRepository;
import com.mongs.lifecycle.service.event.vo.StateCheckEvent;
import com.mongs.lifecycle.service.moduleService.NotificationService;
import com.mongs.lifecycle.service.moduleService.vo.PublishStateVo;
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
public class TaskActiveServiceEventListener {

    private final MongRepository mongRepository;

    private final NotificationService notificationService;

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

        Mong saveMong = mongRepository.save(mong.toBuilder()
                .state(nextState)
                .build());

        notificationService.publishState(saveMong.getAccountId(), PublishStateVo.of(saveMong));
    }
}
