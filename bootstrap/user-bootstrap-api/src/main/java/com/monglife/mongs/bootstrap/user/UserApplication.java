package com.monglife.mongs.bootstrap.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,event,persistence,publish,payment");

        SpringApplication.run(UserApplication.class, args);
    }

}
