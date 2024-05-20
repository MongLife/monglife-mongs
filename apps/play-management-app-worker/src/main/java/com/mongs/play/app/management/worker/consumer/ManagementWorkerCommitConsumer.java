package com.mongs.play.app.management.worker.consumer;

import com.mongs.play.app.management.worker.service.ManagementWorkerService;
import com.mongs.play.core.exception.ErrorException;
import com.mongs.play.module.kafka.event.managementExternal.*;
import com.mongs.play.module.kafka.event.managementInternal.DecreaseStatusEvent;
import com.mongs.play.module.kafka.event.managementInternal.FeedMongEvent;
import com.mongs.play.module.kafka.event.managementInternal.IncreaseStatusEvent;
import com.mongs.play.module.kafka.event.managementInternal.TrainingMongEvent;
import com.mongs.play.module.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagementWorkerCommitConsumer {

    private final KafkaService kafkaService;
    private final ManagementWorkerService managementWorkerService;

    // ##### Management-External #####
    @KafkaListener(topics = { KafkaService.CommitTopic.REGISTER_MONG })
    public void zeroEvolution(RegisterMongEvent payload) {
        try {
            Long mongId = payload.getMongId();

            log.info("[zeroEvolution] mongId: {}", mongId);

            managementWorkerService.zeroEvolution(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.REGISTER_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.FIRST_EVOLUTION_MONG })
    public void firstEvolution(EvolutionMongEvent payload) {
        try {
            Long mongId = payload.getMongId();
            String pastMongCode = payload.getPastMongCode();

            log.info("[firstEvolution] mongId: {}, pastMongCode: {}", mongId, pastMongCode);

            managementWorkerService.firstEvolution(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.FIRST_EVOLUTION_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.EVOLUTION_MONG })
    public void evolution(EvolutionMongEvent payload) {
        try {
            Long mongId = payload.getMongId();
            String pastMongCode = payload.getPastMongCode();

            log.info("[evolution] mongId: {}, pastMongCode: {}", mongId, pastMongCode);

            managementWorkerService.evolution(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.EVOLUTION_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.LAST_EVOLUTION_MONG })
    public void lastEvolution(EvolutionMongEvent payload) {
        try {
            Long mongId = payload.getMongId();
            String pastMongCode = payload.getPastMongCode();

            log.info("[lastEvolution] mongId: {}, pastMongCode: {}", mongId, pastMongCode);

            managementWorkerService.lastEvolution(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.LAST_EVOLUTION_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.SLEEP_SLEEPING_MONG })
    public void sleepSleeping(SleepingMongEvent payload) {
        try {
            Long mongId = payload.getMongId();

            log.info("[sleepSleeping] mongId: {}", mongId);

            managementWorkerService.sleepSleeping(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.SLEEP_SLEEPING_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.AWAKE_SLEEPING_MONG })
    public void awakeSleeping(SleepingMongEvent payload) {
        try {
            Long mongId = payload.getMongId();

            log.info("[awakeSleeping] mongId: {}", mongId);

            managementWorkerService.awakeSleeping(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.AWAKE_SLEEPING_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.DELETE_MONG })
    public void delete(DeleteMongEvent payload) {
        try {
            Long mongId = payload.getMongId();

            log.info("[delete] mongId: {}", mongId);

            managementWorkerService.delete(mongId);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DELETE_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.TRAINING_MONG })
    public void trainingDead(TrainingMongEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Double satiety = payload.getSatiety();
            Double healthy = payload.getHealthy();

            log.info("[trainingDead] mongId: {}, satiety: {}, healthy: {}", mongId, satiety, healthy);

            managementWorkerService.dead(mongId, satiety, healthy);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.TRAINING_MONG, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.FEED_MONG })
    public void feedDead(FeedMongEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Double satiety = payload.getSatiety();
            Double healthy = payload.getHealthy();

            log.info("[feedDead] mongId: {}, satiety: {}, healthy: {}", mongId, satiety, healthy);

            managementWorkerService.dead(mongId, satiety, healthy);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.FEED_MONG, payload);
        }
    }

    // ##### Management-Internal #####
    @KafkaListener(topics = { KafkaService.CommitTopic.DECREASE_STATUS })
    public void decreaseStatusDead(DecreaseStatusEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Double satiety = payload.getSatiety();
            Double healthy = payload.getHealthy();

            log.info("[decreaseStatusDead] mongId: {}, satiety: {}, healthy: {}", mongId, satiety, healthy);

            managementWorkerService.dead(mongId, satiety, healthy);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.DECREASE_STATUS, payload);
        }
    }

    @KafkaListener(topics = { KafkaService.CommitTopic.INCREASE_STATUS })
    public void increaseStatusDead(IncreaseStatusEvent payload) {
        try {
            Long mongId = payload.getMongId();
            Double satiety = payload.getSatiety();
            Double healthy = payload.getHealthy();

            log.info("[increaseStatusDead] mongId: {}, satiety: {}, healthy: {}", mongId, satiety, healthy);

            managementWorkerService.dead(mongId, satiety, healthy);
        } catch (ErrorException e) {
//            kafkaService.sendRollback(KafkaService.KafkaTopic.INCREASE_STATUS, payload);
        }
    }
}
