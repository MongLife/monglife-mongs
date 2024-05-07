package com.mongs.play.module.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value("${application.kafka.base-topic.management-external}")
    private String managementExternalTopic;
    @Value("${application.kafka.base-topic.management-internal}")
    private String managementInternalTopic;
    @Value("${application.kafka.base-topic.management-worker}")
    private String managementWorkerTopic;
    @Value("${application.kafka.base-topic.player-external}")
    private String playerExternalTopic;
    @Value("${application.kafka.base-topic.player-internal}")
    private String playerInternalTopic;

    public String getManagementExternalTopic() {
        return managementExternalTopic;
    }

    public String getManagementInternalTopic() {
        return managementInternalTopic;
    }

    public String getManagementWorkerTopic() {
        return managementWorkerTopic;
    }

    public String getPlayerExternalTopic() {
        return playerExternalTopic;
    }

    public String getPlayerInternalTopic() {
        return playerInternalTopic;
    }
}
