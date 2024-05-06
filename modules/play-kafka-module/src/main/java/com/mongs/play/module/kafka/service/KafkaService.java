package com.mongs.play.module.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value("${application.kafka.group-id.management-external}")
    private String managementExternalTopic;
    @Value("${application.kafka.group-id.management-internal}")
    private String managementInternalTopic;
    @Value("${application.kafka.group-id.management-worker}")
    private String managementWorkerTopic;
    @Value("${application.kafka.group-id.player-external}")
    private String playerExternalTopic;
    @Value("${application.kafka.group-id.player-internal}")
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
