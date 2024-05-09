package com.mongs.play.app.management.external.consumer;


import com.mongs.play.module.kafka.event.commit.EvolutionMongEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementExternalRollbackConsumer {

    @KafkaListener(topics = "rollback.evolutionMong")
    public void evolutionMongRollback(EvolutionMongEvent event) {

        Long accountId = event.getAccountId();
        String mongCode = event.getMongCode();


    }
}
