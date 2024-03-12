//package com.mongs.management.domain.service;
//
//import com.mongs.management.code.TestMongCode;
//import com.mongs.management.domain.mong.entity.Mong;
//import com.mongs.management.domain.mong.repository.MongRepository;
//import com.mongs.management.domain.mong.service.MongService;
//import com.mongs.management.domain.mong.service.dto.InitMong;
//import com.mongs.management.domain.mong.service.dto.TrainingCount;
//import com.mongs.management.exception.ManagementException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Optional;
//import java.util.Random;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("test")
//class MongServiceTest {
//
//    @MockBean
//    private MongRepository mongRepository;
//
//    @Autowired
//    private MongService mongService;
//
//    private InitMong initMong;
//    private Long memberId;
//    private Mong mong;
//
//    @BeforeEach
//    void setUp() {
//        memberId = 1L;
//        initMong = new InitMong(TestMongCode.CH000.getName(), LocalDateTime.of(2024, 2, 5, 22, 0), LocalDateTime.of(2024, 2, 26, 8, 0));
//
//        String sleepTimeStart = timeConverter(initMong.sleepStart());
//        String sleepTimeEnd = timeConverter(initMong.sleepEnd());
//        Boolean sleep = isSleep(sleepTimeStart, sleepTimeEnd);
//
//        mong = Mong.builder()
//                .accountId(memberId)
//                .name(initMong.name())
//                .sleepTime(sleepTimeStart)
//                .wakeUpTime(sleepTimeEnd)
//                .weight(new Random().nextDouble() * 100)
//                .numberOfPoop(10)
//                .isSleeping(sleep)
//                .build();
//
//        when(mongRepository.findManagementByMemberId(memberId)).thenReturn(Optional.of(mong));
//    }
//
//    @Test
//    void createMong_Success() {
//        mongService.createMong(initMong, memberId);
//
//        assertNotNull(mong);
//        assertEquals(mong.getAccountId(),1L);
//        assertEquals(mong.getName(), initMong.name());
//        assertEquals(mong.getSleepTime(), "22:00");
//        assertEquals(mong.getWakeUpTime(), "08:00");
//
//        assertEquals(false, isSleep(mong.getSleepTime(), mong.getWakeUpTime()));;
//
//        assertNotNull(mong.getSleepTime());
//        assertNotNull(mong.getWakeUpTime());
//        assertTrue(mong.getWeight() > 0);
//
//        verify(mongRepository, times(1)).save(any(Mong.class));
//    }
//
//    @Test
//    void notFoundMember() {
//        Long wrongMemberId = 0L;
//        when(mongRepository.findManagementByMemberId(wrongMemberId)).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> mongService.toMongStroke(wrongMemberId))
//                .isInstanceOf(ManagementException.class);
//    }
//
//    @Test
//    void toMongStroke() {
//        mongService.toMongStroke(memberId);
//        assertEquals(mong.getNumberOfStroke(), 1);
//    }
//
//    @Test
//    void toCleanMongsPoop() {
//        mongService.toCleanMongsPoop(memberId);
//        assertEquals(0, mong.getNumberOfPoop());
//    }
//
//    @Test
//    void mongTraining() {
//        TrainingCount trainingCount = new TrainingCount(100);
//        mongService.mongTraining(trainingCount, 1L);
//        assertEquals(100, mong.getNumberOfTraining());
//    }
//
//    @Test
//    void isMongSleep() {
//        mongService.toCheckMongsLifetime(1L);
//
//        boolean sleep = isSleep(mong.getSleepTime(), mong.getWakeUpTime());
//        assertEquals(false, sleep);
//
//        LocalTime startTime = LocalTime.parse(mong.getSleepTime(), DateTimeFormatter.ofPattern("HH:mm"));
//        LocalTime endTime = LocalTime.parse(mong.getWakeUpTime(), DateTimeFormatter.ofPattern("HH:mm"));
//        LocalTime currentTime = LocalTime.of(23, 0);
//
//        if (endTime.isBefore(startTime) && !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime)) {
//            sleep = true;
//        }
//        if(endTime.isBefore(startTime) && !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime)){
//            sleep = false;
//        }
//        assertEquals(true, sleep);
//    }
//
//    private Boolean isSleep(String sleepStart, String sleepEnd) {
//        LocalTime startTime = LocalTime.parse(sleepStart, DateTimeFormatter.ofPattern("HH:mm"));
//        LocalTime endTime = LocalTime.parse(sleepEnd, DateTimeFormatter.ofPattern("HH:mm"));
//        LocalTime currentTime = LocalTime.of(15, 0);
//
//        if (endTime.isBefore(startTime)) {
//            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
//        } else {
//            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
//        }
//    }
//
//    private String timeConverter(LocalDateTime time) {
//        return time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
//    }
//}
