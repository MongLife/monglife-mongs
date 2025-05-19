package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.application.member.port.out.FeedbackPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateFeedbackVo;
import com.monglife.mongs.domain.member.model.Feedback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutMemberPersistenceConfig.class, MemberDataSourceConfig.class, HibernateAutoConfig.class })
class FeedbackPersistenceServiceTest {

    private final FeedbackPersistencePort feedbackPersistencePort;

    @Autowired
    public FeedbackPersistenceServiceTest(FeedbackPersistencePort feedbackPersistencePort) {
        this.feedbackPersistencePort = feedbackPersistencePort;
    }

    private static final Long accountId = 1L;
    private static final String deviceId = CommonUtil.randomId();
    private static final String deviceName = "TEST-DEVICE-NAME";

    @Nested
    @DisplayName("오류 신고 등록 단위 테스트")
    class CreateFeedbackPort {

        @Test
        @DisplayName("오류 신고를 등록 한다.")
        void createFeedback() {
            // arrange
            String title = "TEST-TITLE";
            String content = "TEST-CONTENT";
            CreateFeedbackVo createFeedbackVo = CreateFeedbackVo.builder()
                    .accountId(accountId)
                    .deviceId(deviceId)
                    .deviceName(deviceName)
                    .title(title)
                    .content(content)
                    .build();

            // act
            Optional<Feedback> feedbackOptional = feedbackPersistencePort.createFeedbackPort(createFeedbackVo);

            // assert
            assertTrue(feedbackOptional.isPresent());
        }
    }
}