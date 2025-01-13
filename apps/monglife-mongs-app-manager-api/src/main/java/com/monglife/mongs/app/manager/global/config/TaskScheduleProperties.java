package com.monglife.mongs.app.manager.global.config;

import com.monglife.mongs.domain.mong.dto.etc.DecreaseMongStatusRatioDto;
import com.monglife.mongs.domain.mong.dto.etc.IncreaseMongStatusRatioDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Component
@ConfigurationProperties(prefix = "application.scheduler.task")
public class TaskScheduleProperties {

    public Property eggEvolution;

    public Property sleep;

    public Property wakeup;

    public Property increaseStatus;

    public Property decreaseStatus;

    public Property increasePoop;

    public Property dead;

    /**
     * 속성 클래스
     */
    @Getter
    @Setter
    public static class Property {

        public String code;

        public Long expiration;

        public Double exp;

        public Double weight;

        public Double strengthRatio;

        public Double satietyRatio;

        public Double healthyRatio;

        public Double fatigueRatio;

        public Integer poopCount;

        public IncreaseMongStatusRatioDto toIncreaseMongStatusDto(Double percentage) {
            return IncreaseMongStatusRatioDto.builder()
                    .exp(this.exp * percentage)
                    .weight(this.weight * percentage)
                    .strengthRatio(this.strengthRatio * percentage)
                    .satietyRatio(this.satietyRatio * percentage)
                    .healthyRatio(this.healthyRatio * percentage)
                    .fatigueRatio(this.fatigueRatio * percentage)
                    .poopCount(this.poopCount)
                    .build();
        }

        public DecreaseMongStatusRatioDto toDecreaseMongStatusDto(Double percentage) {
            return DecreaseMongStatusRatioDto.builder()
                    .exp(this.exp * percentage)
                    .weight(this.weight * percentage)
                    .strengthRatio(this.strengthRatio * percentage)
                    .satietyRatio(this.satietyRatio * percentage)
                    .healthyRatio(this.healthyRatio * percentage)
                    .fatigueRatio(this.fatigueRatio * percentage)
                    .poopCount(this.poopCount)
                    .build();
        }
    }
}
