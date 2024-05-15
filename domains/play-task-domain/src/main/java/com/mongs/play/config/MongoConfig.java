package com.mongs.play.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    private final LocalDateTimeToDateKstConverter localDateTimeToDateKstConverter;
    private final DateToLocalDateTimeKstConverter dateToLocalDateTimeKstConverter;

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(localDateTimeToDateKstConverter, dateToLocalDateTimeKstConverter));
    }
}
