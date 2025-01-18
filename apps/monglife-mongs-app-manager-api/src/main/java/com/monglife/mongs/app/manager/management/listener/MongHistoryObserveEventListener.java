package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.client.fcm.service.FcmService;
import com.monglife.mongs.domain.mong.dto.event.MongStateHistoryEvent;
import com.monglife.mongs.domain.mong.dto.event.MongStatusHistoryEvent;
import com.monglife.mongs.domain.mong.entity.MongStateHistoryEntity;
import com.monglife.mongs.domain.mong.entity.MongStatusHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongHistoryObserveEventListener {

    private final FcmService fcmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongStatusHistoryEventListener(MongStatusHistoryEvent event) {

        if (MongStatusHistoryEntity.MongStatusHistoryType.SET_CODE.equals(event.getType())) {

            String title = "";
            String body = "";
            Long accountId = event.getAccountId();
            String mongName = event.getMongName();

            switch (event.getCode()) {
                case SOMNOLENCE -> {
                    title = "졸린 몽이 있어요";
                    body = mongName + "(을)를 재워야 해요";
                }
                case HUNGRY -> {
                    title = "배고픈 몽이 있어요";
                    body = mongName + "에게 밥을 줘야 해요";

                }
                case SICK -> {
                    title = "아픈 몽이 있어요";
                    body = mongName + "의 체력을 채워야 해요";
                }
            }

            if (!title.isBlank() && !body.isBlank()) {
                fcmService.sendPush(accountId, title, body);
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongStateHistoryEventListener(MongStateHistoryEvent event) {

        if (MongStateHistoryEntity.MongStateHistoryType.SET_CODE.equals(event.getType())) {

            String title = "";
            String body = "";
            Long accountId = event.getAccountId();
            String mongName = event.getMongName();

            switch (event.getCode()) {
                case DEAD -> {
                    title = "죽은 몽이 있어요";
                    body = mongName + "(이)가 죽었어요...";
                }

                case EVOLUTION_READY -> {
                    title = "진화 준비가 되었어요";
                    body = mongName + "(을)를 새로운 몽으로 진화시켜 주세요";
                }

                case GRADUATE_READY -> {
                    title = "졸업 준비가 되었어요";
                    body = mongName + "(을)를 졸업 시켜 주세요";
                }
            }

            if (!title.isBlank() && !body.isBlank()) {
                fcmService.sendPush(accountId, title, body);
            }
        }
    }
}
