package com.monglife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class ManagerApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,client,domain,common");

        SpringApplication.run(ManagerApplication.class, args);
    }

}
