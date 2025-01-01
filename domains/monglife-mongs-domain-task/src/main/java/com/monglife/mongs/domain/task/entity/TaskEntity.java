package com.monglife.mongs.domain.task.entity;

import com.monglife.mongs.domain.task.enums.TaskStateCode;
import com.monglife.mongs.domain.task.enums.TaskStatusCode;
import com.monglife.mongs.domain.task.listener.TaskEntityListener;
import com.monglife.mongs.module.jpa.entity.BaseTimeEntity;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class, TaskEntityListener.class })
@Table(name = "mongs_task")
@ToString
public class TaskEntity extends BaseTimeEntity {

    private static final Long MIN_EXPIRATION = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", updatable = false)
    private Long taskId;

    @Column(name = "app_ackage_name", updatable = false)
    private String appPackageName;

    @Column(name = "task_owner_id", updatable = false)
    private String taskOwnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_code", updatable = false)
    private ComnCodeEntity comn;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status_code")
    private TaskStatusCode taskStatusCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_state_code")
    private TaskStateCode taskStateCode;

    @Column(name = "rest_expiration_seconds")
    private Long restExpirationSeconds; // 남은 만료 시간

    @Column(name = "expiration_seconds")
    private Long expirationSeconds;     // 만료 시간

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;    // 만료 시각

    @Column(name = "fix_time", updatable = false)
    private LocalTime fixTime;

    @Transient
    private LocalDateTime now = LocalDateTime.now();

    /**
     * 시간 고정 Task 생성자
     */
    public TaskEntity(String appPackageName, String taskOwnerId, ComnCodeEntity comn, TaskStateCode taskStateCode, LocalTime fixTime) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.comn = comn;
        this.taskStateCode = taskStateCode;
        this.fixTime = fixTime == null ? LocalTime.of(0, 0) : fixTime;
        this.taskStatusCode = TaskStatusCode.PROCESSING;

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
     * 시간 반복 Task 생성자
     */
    public TaskEntity(String appPackageName, String taskOwnerId, ComnCodeEntity comn, TaskStateCode taskStateCode, Long expirationSeconds) {
        this.appPackageName = appPackageName;
        this.taskOwnerId = taskOwnerId;
        this.comn = comn;
        this.taskStateCode = taskStateCode;
        this.expirationSeconds = expirationSeconds;
        this.taskStatusCode = TaskStatusCode.PROCESSING;

        this.restExpirationSeconds = this.expirationSeconds;
        this.expiredAt = LocalDateTime.now().plusSeconds(this.restExpirationSeconds);

        this.now = LocalDateTime.now();
    }

    public Boolean isCycle() {
        return this.taskStateCode.equals(TaskStateCode.FIX_TIME_CYCLE) || this.taskStateCode.equals(TaskStateCode.NONE_FIX_TIME_CYCLE);
    }

    private Boolean isFixTime() {
        return this.taskStateCode.equals(TaskStateCode.FIX_TIME_CYCLE) || this.taskStateCode.equals(TaskStateCode.FIX_TIME);
    }

    public void retry() {

        switch (this.taskStatusCode) {
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

            case PAUSE -> {}

            case APP_STOP_PROCESSING -> {}

            case APP_STOP_PAUSE -> {}
        }
    }

    /**
     * Task 반복
     */
    public void cycle() {

        if (!this.isCycle()) return;

        switch (this.taskStatusCode) {
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

            case PAUSE -> {}

            case APP_STOP_PROCESSING -> {}

            case APP_STOP_PAUSE -> {}
        }
    }

    /**
     * Task 일시 중지
     */
    public void pause() {

        switch (this.taskStatusCode) {
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

                this.taskStatusCode = TaskStatusCode.PAUSE;
            }

            case PAUSE -> {}

            case APP_STOP_PROCESSING -> {}

            case APP_STOP_PAUSE -> {}
        }
    }

    /**
     * Task 재시작
     */
    public void resume() {

        switch (this.taskStatusCode) {
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

                this.taskStatusCode = TaskStatusCode.PROCESSING;
            }
        }
    }

    public void appStopPause() {

        switch (this.taskStatusCode) {
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

                this.taskStatusCode = TaskStatusCode.APP_STOP_PROCESSING;
            }

            case PAUSE -> this.taskStatusCode = TaskStatusCode.APP_STOP_PAUSE;

            case APP_STOP_PROCESSING -> {}

            case APP_STOP_PAUSE -> {}
        }
    }

    public void appStopResume() {

        switch (this.taskStatusCode) {
            case PROCESSING -> {}

            case PAUSE -> {}

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

                this.taskStatusCode = TaskStatusCode.PROCESSING;
            }

            case APP_STOP_PAUSE -> this.taskStatusCode = TaskStatusCode.PAUSE;
        }
    }
}
