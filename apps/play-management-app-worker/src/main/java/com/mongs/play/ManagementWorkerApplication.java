package com.mongs.play;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ManagementWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementWorkerApplication.class, args);
    }

}
