package com.mongs.play;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CommonExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonExternalApplication.class, args);
    }
}
