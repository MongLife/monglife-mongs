package com.monglife.mongs.bootstrap.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(value = {})
public class UserApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,event,persistence,publish,payment");

        SpringApplication.run(UserApplication.class, args);
    }

}
