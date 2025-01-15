package com.monglife.mongs.app.manager.management.listener;

//import com.monglife.mongs.client.fcm.service.FcmService;
import com.monglife.mongs.domain.mong.dto.event.MongHistoryObserveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongHistoryObserveEventListener {

//    private final FcmService fcmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void mongHistoryObserveEventListener(MongHistoryObserveEvent event) {

        // TODO: 상태 변경 시, FCM 전송 기능 구현
    }
}
