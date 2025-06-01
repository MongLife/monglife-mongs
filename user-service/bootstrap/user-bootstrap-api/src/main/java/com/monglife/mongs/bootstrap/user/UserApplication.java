package com.monglife.mongs.bootstrap.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,subscribe,web,event,persistence,publish,payment,schedule");

        SpringApplication.run(UserApplication.class, args);
    }

}
