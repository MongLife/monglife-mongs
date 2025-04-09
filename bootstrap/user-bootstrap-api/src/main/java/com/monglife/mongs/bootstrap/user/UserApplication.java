package com.monglife.mongs.bootstrap.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {})
public class UserApplication {

    public static void main(String[] args) {

        System.setProperty("spring.config.name", "application,adapter,core");

        SpringApplication.run(UserApplication.class, args);
    }

}
