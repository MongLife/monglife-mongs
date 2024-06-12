package com.mongs.play;

import com.mongs.play.client.publisher.battle.service.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@RequiredArgsConstructor
public class BattleWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleWorkerApplication.class, args);
    }

}
