package com.mongs.play.module.task.enums;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Getter
@Component
public class TaskUtil {
    private static final Random random = new Random();

    public static Double subWeight;
    public static Double subStrength;
    public static Double subSatiety;
    public static Double subHealthy;
    public static Double subSleep;

    public static Double addWeight;
    public static Double addStrength;
    public static Double addSatiety;
    public static Double addHealthy;
    public static Double addSleep;

    public static Long zeroEvolutionExpiration;
    public static Long decreaseStatusExpiration;
    public static Long increaseStatusExpiration;
    public static Long increasePoopCountMin;
    public static Long increasePoopCountMax;
    public static Long deadSatiety;
    public static Long deadHealthy;

    public static Long getExpiration(TaskCode taskCode) {
        switch (taskCode) {
            case ZERO_EVOLUTION -> {
                return zeroEvolutionExpiration;
            }
            case DECREASE_STATUS -> {
                return decreaseStatusExpiration;
            }
            case INCREASE_STATUS -> {
                return increaseStatusExpiration;
            }
            case INCREASE_POOP_COUNT -> {
                return random.nextLong(increasePoopCountMin, increasePoopCountMax + 1);
            }
            case DEAD_SATIETY -> {
                return deadSatiety;
            }
            case DEAD_HEALTHY -> {
                return deadHealthy;
            }
            default -> {
                return Long.MAX_VALUE;
            }
        }
    }

    @Value("${env.status.sub.weight}")
    public void setSubWeight(Double subWeight) {
        TaskUtil.subWeight = subWeight;
    }
    @Value("${env.status.sub.strength}")
    public void setSubStrength(Double subStrength) {
        TaskUtil.subStrength = subStrength;
    }
    @Value("${env.status.sub.satiety}")
    public void setSubSatiety(Double subSatiety) {
        TaskUtil.subSatiety = subSatiety;
    }
    @Value("${env.status.sub.healthy}")
    public void setSubHealthy(Double subHealthy) {
        TaskUtil.subHealthy = subHealthy;
    }
    @Value("${env.status.sub.sleep}")
    public void setSubSleep(Double subSleep) {
        TaskUtil.subSleep = subSleep;
    }
    @Value("${env.status.add.weight}")
    public void setAddWeight(Double addWeight) {
        TaskUtil.addWeight = addWeight;
    }
    @Value("${env.status.add.strength}")
    public void setAddStrength(Double addStrength) {
        TaskUtil.addStrength = addStrength;
    }
    @Value("${env.status.add.satiety}")
    public void setAddSatiety(Double addSatiety) {
        TaskUtil.addSatiety = addSatiety;
    }
    @Value("${env.status.add.healthy}")
    public void setAddHealthy(Double addHealthy) {
        TaskUtil.addHealthy = addHealthy;
    }
    @Value("${env.status.add.sleep}")
    public void setAddSleep(Double addSleep) {
        TaskUtil.addSleep = addSleep;
    }

    @Value("${env.expiration.zero_evolution}")
    public void setZeroEvolutionExpiration(Long zeroEvolutionExpiration) {
        TaskUtil.zeroEvolutionExpiration = zeroEvolutionExpiration;
    }
    @Value("${env.expiration.decrease_status}")
    public void setDecreaseStatusExpiration(Long decreaseStatusExpiration) {
        TaskUtil.decreaseStatusExpiration = decreaseStatusExpiration;
    }
    @Value("${env.expiration.increase_status}")
    public void setIncreaseStatusExpiration(Long increaseStatusExpiration) {
        TaskUtil.increaseStatusExpiration = increaseStatusExpiration;
    }
    @Value("${env.expiration.increase_poop_count.min}")
    public void setIncreasePoopCountMin(Long increasePoopCountMin) {
        TaskUtil.increasePoopCountMin = increasePoopCountMin;
    }
    @Value("${env.expiration.increase_poop_count.max}")
    public void setIncreasePoopCountMax(Long increasePoopCountMax) {
        TaskUtil.increasePoopCountMax = increasePoopCountMax;
    }
    @Value("${env.expiration.dead_satiety}")
    public void setDeadSatiety(Long deadSatiety) {
        TaskUtil.deadSatiety = deadSatiety;
    }
    @Value("${env.expiration.dead_healthy}")
    public void setDeadHealthy(Long deadHealthy) {
        TaskUtil.deadHealthy = deadHealthy;
    }
}
