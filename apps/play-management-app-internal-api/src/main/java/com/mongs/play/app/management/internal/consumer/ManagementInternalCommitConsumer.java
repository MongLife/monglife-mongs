package com.mongs.play.app.management.internal.consumer;

import com.mongs.play.core.error.module.KafkaErrorCode;
import com.mongs.play.core.exception.common.KafkaTransactionException;
import com.mongs.play.module.kafka.event.commit.DecreaseWeightCommitPayload;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManagementInternalCommitConsumer {

    @KafkaListener(topics = "#{'${application.kafka.base-topic.management-internal}' + '.commit.decreaseWeight'}")
    public void decreaseWeight(DecreaseWeightCommitPayload payload) {
        log.info("payload: {}", payload);

        throw new KafkaTransactionException(KafkaErrorCode.TEST);
    }
}
