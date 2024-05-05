package com.mongs.play.app.gateway.internal.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${env.webclient.connect-timeout}")
    private int CONNECT_TIMEOUT;
    @Value("${env.webclient.read-timeout}")
    private int READ_TIMEOUT;
    @Value("${env.webclient.write-timeout}")
    private int WRITE_TIMEOUT;

    /** Auth Module **/
    @Value("${env.webclient.routes.auth.url}")
    private String AUTH_URL;

    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Bean("authWebClient")
    @LoadBalanced
    public WebClient authWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .responseTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl("http://" + AUTH_URL)
                .filter(lbFunction)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
