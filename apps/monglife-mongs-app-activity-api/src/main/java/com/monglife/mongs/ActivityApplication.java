package com.monglife.mongs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
public class ActivityApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,client,domain,module");

        SpringApplication.run(ActivityApplication.class, args);
    }

}
