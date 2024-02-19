package com.mongs.gateway.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${application.webclient.connect-timeout}")
    private int CONNECT_TIMEOUT;
    @Value("${application.webclient.read-timeout}")
    private int READ_TIMEOUT;
    @Value("${application.webclient.write-timeout}")
    private int WRITE_TIMEOUT;

    /** Auth Module **/
    @Value("${application.webclient.routes.auth.host}")
    private String AUTH_HOST;
    @Value("${application.webclient.routes.auth.port}")
    private int AUTH_PORT;

    @Bean("authWebClient")
    public WebClient authWebClient() {
        HttpClient httpClient = HttpClient.create()
                .host(AUTH_HOST)
                .port(AUTH_PORT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .responseTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
