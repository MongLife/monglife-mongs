package com.monglife.mongs.adapter.out.mong.schedule.entity;

import com.monglife.module.common.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskStateCode;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskTypeCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "mongs_task")
public class TaskEntity extends BaseTimeEntity {

    private static final Long MIN_EXPIRATION = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", updatable = false)
    private Long taskId;

    @Column(name = "app_ackage_name", updatable = false)
    private String appPackageName;

    @Column(name = "mong_id", updatable = false)
    private Long mongId;

    @Column(name = "account_id", updatable = false)
    private Long accountId;

    @Column(name = "scheduler_type_code")
    private String schedulerTypeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_state_code")
    private TaskStateCode stateCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type_code")
    private TaskTypeCode typeCode;

    @Column(name = "rest_expiration_seconds")
    private Long restExpirationSeconds;

    @Column(name = "expiration_seconds")
    private Long expirationSeconds;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "fix_time", updatable = false)
    private LocalTime fixTime;

    @Transient
    private LocalDateTime now = LocalDateTime.now();

    /**
     * 시간 반복 Task 생성자
     */
    public TaskEntity(String appPackageName, Long mongId, Long accountId, String schedulerTypeCode, TaskTypeCode typeCode, Long expirationSeconds) {
        this.appPackageName = appPackageName;
        this.mongId = mongId;
        this.accountId = accountId;
        this.schedulerTypeCode = schedulerTypeCode;
        this.typeCode = typeCode;
        this.expirationSeconds = expirationSeconds;
        this.stateCode = TaskStateCode.PROCESSING;

        this.restExpirationSeconds = this.expirationSeconds;
        this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);

        this.now = LocalDateTime.now();
    }

    /**
     * 시간 고정 Task 생성자
     */
    public TaskEntity(String appPackageName, Long mongId, Long accountId, String schedulerTypeCode, TaskTypeCode typeCode, LocalTime fixTime) {
        this.appPackageName = appPackageName;
        this.mongId = mongId;
        this.accountId = accountId;
        this.schedulerTypeCode = schedulerTypeCode;
        this.typeCode = typeCode;
        // 밀리초를 남기면 안 된다. Hibernate 6.2.5 의 LocalTimeJavaType.wrap 은
        // java.sql.Time.getTime() % 1000 의 음수 나머지를 보정하지 않고 NANO_OF_SECOND 에 넣는다.
        // KST 는 09시 이전 시각의 epoch millis 가 음수라, 그런 값이 저장되면 이 행을 읽는
        // 모든 쿼리가 DateTimeException 으로 터진다. 스케줄은 초 단위라 잘라도 무손실이다.
        this.fixTime = (fixTime == null ? LocalTime.of(0, 0) : fixTime).withNano(0);
        this.stateCode = TaskStateCode.PROCESSING;

        this.expiredAt = LocalDateTime.of(this.now.toLocalDate(), this.fixTime);

        if (this.expiredAt.isBefore(this.now)) {
            this.expiredAt = this.expiredAt.plusDays(1);
            this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
        } else if (this.expiredAt.isEqual(this.now)) {
            this.expirationSeconds = 1L;
        } else {
            this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
        }

        this.restExpirationSeconds = this.expirationSeconds;

        this.now = LocalDateTime.now();
    }

    /**
     * Task 반복 여부 확인
     * @return Task 반복 여부
     */
    public Boolean isCycle() {
        return this.typeCode.equals(TaskTypeCode.FIX_TIME_CYCLE) || this.typeCode.equals(TaskTypeCode.NONE_FIX_TIME_CYCLE);
    }

    /**
     * Task 고정 시간 여부
     * @return 고정 시간 여부
     */
    private Boolean isFixTime() {
        return this.typeCode.equals(TaskTypeCode.FIX_TIME_CYCLE) || this.typeCode.equals(TaskTypeCode.FIX_TIME);
    }

    /**
     * Task 반복
     */
    public void cycle() {

        if (!this.isCycle()) return;

        switch (this.stateCode) {
            case PROCESSING -> {
                if (this.isFixTime()) {
                    // 만료 시각 + 1일 - 정해진 시간
                    this.expiredAt = LocalDateTime.of(this.expiredAt.toLocalDate().plusDays(1), this.fixTime);
                    this.restExpirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                } else {
                    // 만료 시간
                    this.restExpirationSeconds = this.expirationSeconds;
                    this.expiredAt = this.now.plusSeconds(this.restExpirationSeconds);
                }
            }
            case PAUSE, APP_STOP_PROCESSING, APP_STOP_PAUSE -> {}
        }
    }

    /**
     * @hidden
     * Task 일시 중지
     */
    public void pause() {
        switch (this.stateCode) {
            case PROCESSING -> {
                if (this.isFixTime()) {
                    this.expirationSeconds = null;
                    this.restExpirationSeconds = null;
                    this.expiredAt = null;
                } else {
                    this.restExpirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    this.restExpirationSeconds =  Math.max(MIN_EXPIRATION, this.restExpirationSeconds);
                    this.expiredAt = null;
                }

                this.updateTaskStatusCode(TaskStateCode.PAUSE);
            }
            case PAUSE, APP_STOP_PAUSE, APP_STOP_PROCESSING -> {}
        }
    }

    /**
     * @hidden
     * Task 재시작
     */
    public void resume() {
        switch (this.stateCode) {
            case PROCESSING -> {}
            case PAUSE, APP_STOP_PROCESSING, APP_STOP_PAUSE -> {
                if (this.isFixTime()) {
                    this.expiredAt = LocalDateTime.of(this.now.toLocalDate(), this.fixTime);

                    if (this.expiredAt.isBefore(this.now)) {
                        this.expiredAt = this.expiredAt.plusDays(1);
                        this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    } else if (this.expiredAt.isEqual(this.now)) {
                        this.expirationSeconds = MIN_EXPIRATION;
                    } else {
                        this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    }

                    this.restExpirationSeconds = this.expirationSeconds;

                } else {
                    this.expiredAt = this.now.plusSeconds(this.restExpirationSeconds);
                }

                this.updateTaskStatusCode(TaskStateCode.PROCESSING);
            }
        }
    }

    /**
     * 앱 중단 Task 일시 중지
     */
    public void appStopPause() {
        switch (this.stateCode) {
            case PROCESSING -> {
                if (this.isFixTime()) {
                    this.expirationSeconds = null;
                    this.restExpirationSeconds = null;
                    this.expiredAt = null;
                } else {
                    this.restExpirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    this.restExpirationSeconds =  Math.max(MIN_EXPIRATION, this.restExpirationSeconds);
                    this.expiredAt = null;
                }

                this.updateTaskStatusCode(TaskStateCode.APP_STOP_PROCESSING);
            }
            case PAUSE -> this.updateTaskStatusCode(TaskStateCode.APP_STOP_PAUSE);
            case APP_STOP_PROCESSING, APP_STOP_PAUSE -> {}
        }
    }

    /**
     * 앱 재기동 Task 재시작
     */
    public void appStopResume() {

        switch (this.stateCode) {
            case APP_STOP_PROCESSING -> {
                if (this.isFixTime()) {
                    this.expiredAt = LocalDateTime.of(this.now.toLocalDate(), this.fixTime);

                    if (this.expiredAt.isBefore(this.now)) {
                        this.expiredAt = this.expiredAt.plusDays(1);
                        this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    } else if (this.expiredAt.isEqual(this.now)) {
                        this.expirationSeconds = MIN_EXPIRATION;
                    } else {
                        this.expirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    }

                    this.restExpirationSeconds = this.expirationSeconds;

                } else {
                    this.restExpirationSeconds = Math.max(5, this.restExpirationSeconds);
                    this.expiredAt = this.now.plusSeconds(this.restExpirationSeconds);
                }

                this.updateTaskStatusCode(TaskStateCode.PROCESSING);
            }
            case APP_STOP_PAUSE -> this.updateTaskStatusCode(TaskStateCode.PAUSE);
            case PROCESSING, PAUSE -> {}
        }
    }

    /**
     * Task 복구
     */
    public void retry() {
        switch (this.stateCode) {
            case PROCESSING -> {
                if (this.isCycle()) {
                    this.cycle();
                } else {
                    if (this.isFixTime()) {
                        this.expiredAt = LocalDateTime.of(this.expiredAt.toLocalDate().plusDays(1), this.fixTime);
                        this.restExpirationSeconds = Duration.between(this.now, this.expiredAt).toSeconds();
                    } else {
                        this.restExpirationSeconds = this.expirationSeconds;
                        this.expiredAt = this.now.plusSeconds(this.restExpirationSeconds);
                    }
                }
            }
            case PAUSE, APP_STOP_PROCESSING, APP_STOP_PAUSE -> {}
        }
    }

    private void updateTaskStatusCode(TaskStateCode taskStateCode) {
        this.stateCode = taskStateCode;
    }
}
