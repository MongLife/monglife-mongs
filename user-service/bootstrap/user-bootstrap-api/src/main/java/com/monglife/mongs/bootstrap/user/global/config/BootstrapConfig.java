package com.monglife.mongs.bootstrap.user.global.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.monglife.mongs")
@EnableDiscoveryClient
public class BootstrapConfig {
}
