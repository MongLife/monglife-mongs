package com.monglife.mongs.app.activity.battle.config;

import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Component
@ConfigurationProperties(prefix = "application.service.battle")
public class BattleProperties {

    public Double exp;

    public Integer rewardPayPoint;

    public Integer bettingPayPoint;
}
