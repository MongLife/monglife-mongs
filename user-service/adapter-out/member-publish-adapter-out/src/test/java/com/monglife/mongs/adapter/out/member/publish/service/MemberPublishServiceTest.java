package com.monglife.mongs.adapter.out.member.publish.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.monglife.module.mqtt.config.MqttAutoConfig;
import com.monglife.mongs.adapter.out.member.publish.config.AdapterOutMemberPublishConfig;
import com.monglife.mongs.adapter.out.member.publish.consumer.MemberSlotCountConsumer;
import com.monglife.mongs.adapter.out.member.publish.consumer.MemberStarPointConsumer;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberSlotCountPublishDto;
import com.monglife.mongs.adapter.out.member.publish.dto.response.MemberStarPointPublishDto;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.domain.member.model.Player;
import com.monglife.mongs.adapter.out.member.publish.utils.MqttTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutMemberPublishConfig.class,
        MqttAutoConfig.class
})
class MemberPublishServiceTest extends MqttTestContainer {

    private final MemberPublishPort memberPublishPort;

    @Autowired
    public MemberPublishServiceTest(MemberPublishPort memberPublishPort) {
        this.memberPublishPort = memberPublishPort;
    }

    @Nested
    @DisplayName("회원 스타 포인트 비동기 응답 단위 테스트")
    class PublishMemberStarPointPort {

        @Autowired
        private MemberStarPointConsumer memberSlotCountConsumer;

        private static Long ACCOUNT_ID;

        @BeforeEach
        void beforeEach() {
            ACCOUNT_ID = (long) RandomUtil.getPositiveInt();
        }

        @Test
        @DisplayName("회원 스타 포인트 변동 사항을 사용자의 기기로 비동기 전송 한다.")
        void publishStarPoint() throws InterruptedException {
            // arrange
            final int starPoint = 100;
            final int slotCount = 1;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .starPoint(starPoint)
                    .slotCount(slotCount)
                    .build();

            MemberStarPointPublishDto memberStarPointPublishDto = new MemberStarPointPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            memberSlotCountConsumer.reset(ACCOUNT_ID, memberStarPointPublishDto, countDownLatch);

            // act
            memberPublishPort.publishStarPointPort(player);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(ACCOUNT_ID, memberStarPointPublishDto.getAccountId());
            assertEquals(starPoint, memberStarPointPublishDto.getStarPoint());
        }
    }

    @Nested
    @DisplayName("회원 슬롯 수 비동기 응답 단위 테스트")
    class PublishMemberSlotCountPort {

        @Autowired
        private MemberSlotCountConsumer memberSlotCountConsumer;

        private static Long ACCOUNT_ID;

        @BeforeEach
        void beforeEach() {
            ACCOUNT_ID = (long) RandomUtil.getPositiveInt();
        }

        @Test
        @DisplayName("회원 슬롯 수 변동 사항을 사용자의 기기로 비동기 전송 한다.")
        void publishSlotCount() throws InterruptedException {
            // arrange
            final int starPoint = 100;
            final int slotCount = 5;
            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .starPoint(starPoint)
                    .slotCount(slotCount)
                    .build();

            MemberSlotCountPublishDto memberSlotCountPublishDto = new MemberSlotCountPublishDto();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            memberSlotCountConsumer.reset(ACCOUNT_ID, memberSlotCountPublishDto, countDownLatch);

            // act
            memberPublishPort.publishSlotCountPort(player);

            var expected = countDownLatch.await(5, TimeUnit.SECONDS);

            // assert
            assertTrue(expected);
            assertEquals(ACCOUNT_ID, memberSlotCountPublishDto.getAccountId());
            assertEquals(slotCount, memberSlotCountPublishDto.getSlotCount());
        }
    }
}