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

    private SchedulerProperty eggEvolution;

    private SchedulerProperty sleep;

    private SchedulerProperty wakeup;

    private SchedulerProperty statusIncrease;

    private SchedulerProperty statusDecrease;

    private SchedulerProperty poopIncrease;

    private SchedulerProperty dead;

    /**
     * code
     */
    public String getEggEvolutionCode() {
        return this.eggEvolution.getCode();
    }

    public String getSleepCode() {
        return this.sleep.getCode();
    }

    public String getWakeupCode() {
        return this.wakeup.getCode();
    }

    public String getStatusIncreaseCode() {
        return this.statusIncrease.getCode();
    }

    public String getStatusDecreaseCode() {
        return this.statusDecrease.getCode();
    }

    public String getPoopIncreaseCode() {
        return this.poopIncrease.getCode();
    }

    public String getDeadCode() {
        return this.dead.getCode();
    }

    /**
     * expiration
     */
    public Long getEggEvolutionExpiration() {
        return this.eggEvolution.getExpiration();
    }

    public Long getStatusIncreaseExpiration() {
        return this.statusIncrease.getExpiration();
    }

    public Long getStatusDecreaseExpiration() {
        return this.statusDecrease.getExpiration();
    }

    public Long getPoopIncreaseExpiration() {
        return this.poopIncrease.getExpiration();
    }

    public Long getDeadExpiration() {
        return this.dead.getExpiration();
    }

    /**
     * data
     */
    public IncreaseMongStatusRatioDto getIncreaseMongStatusDto(Double ratio) {
        return this.statusIncrease.toIncreaseMongStatusDto(ratio);
    }

    public DecreaseMongStatusRatioDto getDecreaseMongStatusDto(Double ratio) {
        return this.statusDecrease.toDecreaseMongStatusDto(ratio);
    }

    public Integer getIncreasePoopCount(Double ratio) {
        return ratio >= 0.5 ? this.poopIncrease.poopCount : 0;
    }

    public Double getDeadSatietyRatio() {
        return this.dead.getSatietyRatio();
    }

    public Double getDeadHealthyRatio() {
        return this.dead.getHealthyRatio();
    }

    @Getter
    @Setter
    private static class SchedulerProperty {

        private String code;

        private Long expiration;

        private Double exp;

        private Double weight;

        private Double strengthRatio;

        private Double satietyRatio;

        private Double healthyRatio;

        private Double fatigueRatio;

        private Integer poopCount;

        public IncreaseMongStatusRatioDto toIncreaseMongStatusDto(Double ratio) {
            return IncreaseMongStatusRatioDto.builder()
                    .exp(this.exp * ratio)
                    .weight(this.weight * ratio)
                    .strengthRatio(this.strengthRatio * ratio)
                    .satietyRatio(this.satietyRatio * ratio)
                    .healthyRatio(this.healthyRatio * ratio)
                    .fatigueRatio(this.fatigueRatio * ratio)
                    .poopCount(this.poopCount)
                    .build();
        }

        public DecreaseMongStatusRatioDto toDecreaseMongStatusDto(Double ratio) {
            return DecreaseMongStatusRatioDto.builder()
                    .exp(this.exp * ratio)
                    .weight(this.weight * ratio)
                    .strengthRatio(this.strengthRatio * ratio)
                    .satietyRatio(this.satietyRatio * ratio)
                    .healthyRatio(this.healthyRatio * ratio)
                    .fatigueRatio(this.fatigueRatio * ratio)
                    .poopCount(this.poopCount)
                    .build();
        }
    }
}
