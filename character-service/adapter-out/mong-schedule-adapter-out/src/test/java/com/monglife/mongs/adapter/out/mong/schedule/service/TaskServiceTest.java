package com.monglife.mongs.adapter.out.mong.schedule.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.config.JpaAuditingAutoConfig;
import com.monglife.module.common.kafka.config.KafkaAutoConfig;
import com.monglife.mongs.adapter.out.mong.schedule.config.AdapterOutMongScheduleConfig;
import com.monglife.mongs.adapter.out.mong.schedule.config.TaskDataSourceConfig;
import com.monglife.mongs.adapter.out.mong.schedule.consumer.TestEventConsumer;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TaskStateCode;
import com.monglife.mongs.adapter.out.mong.schedule.enums.TestSchedulerType;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskRepository;
import com.monglife.mongs.adapter.out.mong.schedule.repository.TaskScheduleRepository;
import com.monglife.mongs.adapter.transaction.TestEventDto;
import com.monglife.mongs.application.mong.port.out.MongSchedulerPort;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutMongScheduleConfig.class,
        TaskDataSourceConfig.class,
        HibernateAutoConfig.class,
        JpaAuditingAutoConfig.class,
        KafkaAutoConfig.class,
        TestEventConsumer.class,
})
@EmbeddedKafka(partitions = 1, bootstrapServersProperty = "module.kafka.url")
@DirtiesContext
class TaskServiceTest {

    /**
     * 스케줄 등록에 드는 시간을 감안한 여유.
     *
     * TaskEntity 는 fixTime 이 등록 시점보다 앞서면 만료를 다음 날로 미룬다(plusDays(1)).
     * 목표 시각을 촉박하게 잡으면 첫 JPA 쿼리·스키마 초기화에 밀려 그 시각이 지나 버리고,
     * 스케줄이 내일로 넘어가 테스트 대기 시간 안에 돌지 않는다.
     */
    private static final long LEAD_SECONDS = 15L;

    /**
     * 이벤트 대기에 주는 추가 여유. 이벤트가 오면 즉시 반환하므로 정상 경로는 느려지지 않고,
     * 머신이 느릴 때만 더 기다린다.
     */
    private static final long AWAIT_SLACK_SECONDS = 10L;

    private final MongSchedulerPort mongSchedulerPort;

    private final TaskRepository taskRepository;

    private final TaskScheduleRepository taskScheduleRepository;

    @Autowired
    TaskServiceTest(MongSchedulerPort mongSchedulerPort, TaskRepository taskRepository, TaskScheduleRepository taskScheduleRepository) {
        this.mongSchedulerPort = mongSchedulerPort;
        this.taskRepository = taskRepository;
        this.taskScheduleRepository = taskScheduleRepository;
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
        taskScheduleRepository.deleteAll();
    }

    @Nested
    @DisplayName("일회성 몽 스케줄 테스크 등록 단위 테스트")
    class CreateTaskPort {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Test
        @DisplayName("일회성 몽 스케줄 테스크를 등록 후 스케줄이 실행되고 테스크 엔티티와 테스크 스케줄이 삭제 된다.")
        void createTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final TestSchedulerType schedulerType = TestSchedulerType.CREATE_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createTaskPort(mongId, accountId, schedulerType);
            var expected2 = countDownLatch.await(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertTrue(expected2);
            assertEquals(expected1.get(), testEventDto.getTaskId());

            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertTrue(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertTrue(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("반복성 몽 스케줄 테스크 등록 단위 테스트")
    class CreateCycleTaskPort {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Test
        @DisplayName("반복성 몽 스케줄 테스크를 등록 후 3번의 스케줄이 실행되고 테스크 엔티티와 테스크 스케줄이 삭제되지 않는다.")
        void createCycleTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final int cycleCount = 3;
            final TestSchedulerType schedulerType = TestSchedulerType.CREATE_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(cycleCount);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createCycleTaskPort(mongId, accountId, schedulerType);
            var expected2 = countDownLatch.await(schedulerType.getExpiration() * cycleCount * 2 + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertFalse(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertFalse(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("고정 시간 반복성 몽 스케줄 테스크 등록 단위 테스트")
    class CreateFixedTimeCycleTaskPort {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Test
        @DisplayName("고정 시간 반복성 몽 스케줄을 등록 후 스케줄이 실행되고 다음 날 같은 시간의 테스크 스케줄로 변경 된다.")
        void createFixedTimeCycleTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final TestSchedulerType schedulerType = TestSchedulerType.CREATE_TEST;
            // 자정을 넘겨도 날짜가 어긋나지 않도록 시각과 날짜를 같은 기준으로 잡는다.
            final LocalDateTime base = LocalDateTime.now().plusSeconds(LEAD_SECONDS);
            final LocalTime fixedTime = base.toLocalTime();
            final LocalDateTime expiredAt = LocalDateTime.of(base.toLocalDate().plusDays(1), fixedTime);

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            testEventConsumer.reset(testEventDto, countDownLatch);

            var expected1 = mongSchedulerPort.createFixedTimeCycleTaskPort(mongId, accountId, schedulerType, fixedTime);
            // 스케줄은 LEAD_SECONDS 뒤에 돈다. 그 시각을 기준으로 기다린다.
            var expected2 = countDownLatch.await(LEAD_SECONDS + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> taskRepository.findByTaskId(expected1.orElse(-1L)).ifPresentOrElse(taskScheduleEntity ->
                            assertTrue(Math.abs(Duration.between(taskScheduleEntity.getExpiredAt(), expiredAt).toSeconds()) <= 1), Assertions::fail));
            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).ifPresentOrElse(taskScheduleEntity ->
                            assertTrue(Math.abs(Duration.between(taskScheduleEntity.getExpiredAt(), expiredAt).toSeconds()) <= 1), Assertions::fail));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 삭제 단위 테스트")
    class DeleteTaskPort {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Test
        @DisplayName("스케줄 테스크를 삭제 한다.")
        void deleteTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final int cycleCount = 2;
            final TestSchedulerType schedulerType = TestSchedulerType.DELETE_1_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(cycleCount);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createCycleTaskPort(mongId, accountId, schedulerType);
            var expected2 = countDownLatch.await(schedulerType.getExpiration() * cycleCount * 2 + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);
            mongSchedulerPort.deleteTaskPort(mongId, schedulerType);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertTrue(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.getExpiration() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertTrue(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 전체 삭제 단위 테스트")
    class DeleteAllTaskPort {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Test
        @DisplayName("모든 스케줄 테스크를 삭제 한다.")
        void deleteAllTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final List<TestSchedulerType> schedulerType = List.of(TestSchedulerType.DELETE_1_TEST, TestSchedulerType.DELETE_2_TEST);

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(schedulerType.size());
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            List<Optional<Long>> expected1 = new ArrayList<>();
            for (TestSchedulerType testSchedulerType : schedulerType) {
                expected1.add(mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType));
            }

            var expected2 = countDownLatch.await(schedulerType.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2 + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);
            mongSchedulerPort.deleteAllTaskPort(mongId);

            // assert
            expected1.forEach(optional -> assertFalse(optional.isEmpty()));
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2 + AWAIT_SLACK_SECONDS))
                            .untilAsserted(() -> assertEquals(0, taskRepository.count()));
            Awaitility.waitAtMost(Duration.ofSeconds(schedulerType.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2 + AWAIT_SLACK_SECONDS))
                    .untilAsserted(() -> assertEquals(0, taskScheduleRepository.count()));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 전체 일시 중지 후 재기동 단위 테스트")
    class AppStop {

        @Autowired
        private TestEventConsumer testEventConsumer;

        @Autowired
        private TaskService taskService;

        @Test
        @DisplayName("테스크 스케줄을 전체 일시 중지하고, 재기동하여 기존 상태를 유지 한다.")
        void appStopPauseAndResumeAllTask() throws InterruptedException {
            // arrange
            final long mongId = 1L;
            final long accountId = 1L;
            final List<TestSchedulerType> schedulerTypes = List.of(
                    TestSchedulerType.APP_STOP_1_TEST,
                    TestSchedulerType.APP_STOP_2_TEST,
                    TestSchedulerType.APP_STOP_3_TEST,
                    TestSchedulerType.APP_STOP_4_TEST,
                    TestSchedulerType.APP_STOP_5_TEST);

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(schedulerTypes.size());
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            List<Optional<Long>> expected1 = new ArrayList<>();
            for (TestSchedulerType testSchedulerType : schedulerTypes) {
                expected1.add(mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType));
            }

            var expected2 = countDownLatch.await(schedulerTypes.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2 + AWAIT_SLACK_SECONDS, TimeUnit.SECONDS);
            taskService.appStopPauseAllTask();

            var expected3 = List.copyOf(taskRepository.findAll());
            var expected4 = taskScheduleRepository.count();

            taskService.appStopResumeAllTask();

            var expected5 = List.copyOf(taskRepository.findAll());
            var expected6 =  taskScheduleRepository.count();

            // assert
            expected1.forEach(optional -> assertFalse(optional.isEmpty()));
            assertTrue(expected2);

            expected3.forEach(taskEntity -> assertEquals(TaskStateCode.APP_STOP_PROCESSING, taskEntity.getStateCode()));
            assertEquals(0, expected4);

            expected5.forEach(taskEntity -> assertEquals(TaskStateCode.PROCESSING, taskEntity.getStateCode()));
            assertEquals(schedulerTypes.size(), expected6);
        }
    }
}