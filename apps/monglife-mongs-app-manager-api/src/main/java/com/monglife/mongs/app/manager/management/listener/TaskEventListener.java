package com.monglife.mongs.app.manager.management.listener;

import com.monglife.mongs.domain.task.dto.event.TaskStopEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventListener {

    @Value("${application.app-code}")
    private String APP_CODE;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void taskStopEventListener(TaskStopEvent event) {

        log.info("{} -> {}", APP_CODE, event);

        // TODO: 앱 코드 확인

        // TODO: 테스크 코드 확인

        // TODO: 적절한 메서드 호출
    }
}
