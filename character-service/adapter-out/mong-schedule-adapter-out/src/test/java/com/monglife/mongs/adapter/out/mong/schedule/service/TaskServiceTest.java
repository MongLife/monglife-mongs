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
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
class TaskServiceTest {

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

        private final TestEventConsumer testEventConsumer;

        @Autowired
        public CreateTaskPort(TestEventConsumer testEventConsumer) {
            this.testEventConsumer = testEventConsumer;
        }

        @Test
        @DisplayName("일회성 몽 스케줄 테스크를 등록 후 스케줄이 실행되고 테스크 엔티티와 테스크 스케줄이 삭제 된다.")
        void createTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            TestSchedulerType testSchedulerType = TestSchedulerType.CREATE_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createTaskPort(mongId, accountId, testSchedulerType);
            var expected2 = countDownLatch.await(testSchedulerType.getExpiration() * 2, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertTrue(expected2);
            assertEquals(expected1.get(), testEventDto.getTaskId());

            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertTrue(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertTrue(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("반복성 몽 스케줄 테스크 등록 단위 테스트")
    class CreateCycleTaskPort {

        private final TestEventConsumer testEventConsumer;

        @Autowired
        public CreateCycleTaskPort(TestEventConsumer testEventConsumer) {
            this.testEventConsumer = testEventConsumer;
        }

        @Test
        @DisplayName("반복성 몽 스케줄 테스크를 등록 후 3번의 스케줄이 실행되고 테스크 엔티티와 테스크 스케줄이 삭제되지 않는다.")
        void createCycleTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            int cycleCount = 3;
            TestSchedulerType testSchedulerType = TestSchedulerType.CREATE_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(cycleCount);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType);
            var expected2 = countDownLatch.await(testSchedulerType.getExpiration() * cycleCount * 2, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertFalse(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertFalse(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("고정 시간 반복성 몽 스케줄 테스크 등록 단위 테스트")
    class CreateFixedTimeCycleTaskPort {

        private final TestEventConsumer testEventConsumer;

        @Autowired
        public CreateFixedTimeCycleTaskPort(TestEventConsumer testEventConsumer) {
            this.testEventConsumer = testEventConsumer;
        }

        @Test
        @DisplayName("고정 시간 반복성 몽 스케줄을 등록 후 스케줄이 실행되고 다음 날 같은 시간의 테스크 스케줄로 변경 된다.")
        void createFixedTimeCycleTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            TestSchedulerType testSchedulerType = TestSchedulerType.CREATE_TEST;
            LocalTime fixedTime = LocalTime.now().plusSeconds(testSchedulerType.getExpiration());
            LocalDateTime expiredAt = LocalDateTime.of(LocalDate.now().plusDays(1), fixedTime);

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            testEventConsumer.reset(testEventDto, countDownLatch);

            var expected1 = mongSchedulerPort.createFixedTimeCycleTaskPort(mongId, accountId, testSchedulerType, fixedTime);
            var expected2 = countDownLatch.await(testSchedulerType.getExpiration() * 2, TimeUnit.SECONDS);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> taskRepository.findByTaskId(expected1.orElse(-1L)).ifPresentOrElse(taskScheduleEntity ->
                            assertTrue(Math.abs(Duration.between(taskScheduleEntity.getExpiredAt(), expiredAt).toSeconds()) <= 1), Assertions::fail));
            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).ifPresentOrElse(taskScheduleEntity ->
                            assertTrue(Math.abs(Duration.between(taskScheduleEntity.getExpiredAt(), expiredAt).toSeconds()) <= 1), Assertions::fail));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 삭제 단위 테스트")
    class DeleteTaskPort {

        private final TestEventConsumer testEventConsumer;

        @Autowired
        public DeleteTaskPort(TestEventConsumer testEventConsumer) {
            this.testEventConsumer = testEventConsumer;
        }

        @Test
        @DisplayName("스케줄 테스크를 삭제 한다.")
        void deleteTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            int cycleCount = 2;
            TestSchedulerType testSchedulerType = TestSchedulerType.DELETE_1_TEST;

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(cycleCount);
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            var expected1 = mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType);
            var expected2 = countDownLatch.await(testSchedulerType.getExpiration() * cycleCount * 2, TimeUnit.SECONDS);
            mongSchedulerPort.deleteTaskPort(mongId, testSchedulerType);

            // assert
            assertFalse(expected1.isEmpty());
            assertEquals(expected1.get(), testEventDto.getTaskId());
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertTrue(taskRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerType.getExpiration() * 2))
                    .untilAsserted(() -> assertTrue(taskScheduleRepository.findByTaskId(expected1.orElse(-1L)).isEmpty()));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 전체 삭제 단위 테스트")
    class DeleteAllTaskPort {

        private final TestEventConsumer testEventConsumer;

        @Autowired
        public DeleteAllTaskPort(TestEventConsumer testEventConsumer) {
            this.testEventConsumer = testEventConsumer;
        }

        @Test
        @DisplayName("모든 스케줄 테스크를 삭제 한다.")
        void deleteAllTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            List<TestSchedulerType> testSchedulerTypes = List.of(TestSchedulerType.DELETE_1_TEST, TestSchedulerType.DELETE_2_TEST);

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(testSchedulerTypes.size());
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            List<Optional<Long>> expected1 = new ArrayList<>();
            for (TestSchedulerType testSchedulerType : testSchedulerTypes) {
                expected1.add(mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType));
            }

            var expected2 = countDownLatch.await(testSchedulerTypes.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2, TimeUnit.SECONDS);
            mongSchedulerPort.deleteAllTaskPort(mongId);

            // assert
            expected1.forEach(optional -> assertFalse(optional.isEmpty()));
            assertTrue(expected2);

            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerTypes.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2))
                            .untilAsserted(() -> assertEquals(0, taskRepository.count()));
            Awaitility.waitAtMost(Duration.ofSeconds(testSchedulerTypes.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2))
                    .untilAsserted(() -> assertEquals(0, taskScheduleRepository.count()));
        }
    }

    @Nested
    @DisplayName("스케줄 테스크 전체 일시 중지 후 재기동 단위 테스트")
    class AppStop {

        private final TestEventConsumer testEventConsumer;

        private final TaskService taskService;

        @Autowired
        public AppStop(TestEventConsumer testEventConsumer, TaskService taskService) {
            this.testEventConsumer = testEventConsumer;
            this.taskService = taskService;
        }

        @Test
        @DisplayName("테스크 스케줄을 전체 일시 중지하고, 재기동하여 기존 상태를 유지 한다.")
        void appStopPauseAndResumeAllTask() throws InterruptedException {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            List<TestSchedulerType> testSchedulerTypes = List.of(
                    TestSchedulerType.APP_STOP_1_TEST,
                    TestSchedulerType.APP_STOP_2_TEST,
                    TestSchedulerType.APP_STOP_3_TEST,
                    TestSchedulerType.APP_STOP_4_TEST,
                    TestSchedulerType.APP_STOP_5_TEST
            );

            TestEventDto testEventDto = new TestEventDto();
            CountDownLatch countDownLatch = new CountDownLatch(testSchedulerTypes.size());
            testEventConsumer.reset(testEventDto, countDownLatch);

            // act
            List<Optional<Long>> expected1 = new ArrayList<>();
            for (TestSchedulerType testSchedulerType : testSchedulerTypes) {
                expected1.add(mongSchedulerPort.createCycleTaskPort(mongId, accountId, testSchedulerType));
            }

            var expected2 = countDownLatch.await(testSchedulerTypes.stream().mapToLong(TestSchedulerType::getExpiration).sum() * 2, TimeUnit.SECONDS);
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
            assertEquals(testSchedulerTypes.size(), expected6);
        }
    }
}