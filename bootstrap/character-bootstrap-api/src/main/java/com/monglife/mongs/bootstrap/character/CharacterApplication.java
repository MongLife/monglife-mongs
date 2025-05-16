package com.monglife.mongs.bootstrap.character;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CharacterApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,event,persistence,publish");

        SpringApplication.run(CharacterApplication.class, args);
    }

}
