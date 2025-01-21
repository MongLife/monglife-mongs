package com.monglife.mongs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,client,domain,module");

        SpringApplication.run(UserApplication.class, args);
    }

}
