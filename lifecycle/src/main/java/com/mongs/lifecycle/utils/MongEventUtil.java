package com.mongs.lifecycle.utils;

import com.mongs.lifecycle.code.EventStatusCode;
import com.mongs.lifecycle.code.MongEventCode;
import com.mongs.lifecycle.entity.MongEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MongEventUtil {

    private final Integer MIN_POOP_EXPIRATION = 5;//60 * 60 * 4;

    public MongEvent generateMongEvent(Long mongId, MongEventCode mongEventCode) {
        long expiration = mongEventCode.getExpiration();

        if (MongEventCode.POOP.equals(mongEventCode)) {
            expiration = (int) (Math.random() * (expiration - MIN_POOP_EXPIRATION + 1) + MIN_POOP_EXPIRATION);
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiredAt = createdAt.plusSeconds(expiration);

        return MongEvent.builder()
                .eventId(mongId + "_" + mongEventCode.name())
                .mongId(mongId)
                .eventCode(mongEventCode)
                .expiration(expiration)
                .expiredAt(expiredAt)
                .createdAt(createdAt)
                .statusCode(EventStatusCode.WAIT)
                .build();
    }
}
