package com.monglife.mongs.domain.mong.listener;

import com.monglife.mongs.domain.mong.dto.event.MongEvolutionEvent;
import com.monglife.mongs.domain.mong.service.MongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MongEvolutionEventListener {

    private final MongService mongService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void mongEvolutionEventListener(MongEvolutionEvent event) {

        mongService.evolutionReadyMong(event.getMongId());
    }
}
