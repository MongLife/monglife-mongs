package com.mongs.member.domain.feedback.repository;

import com.mongs.member.domain.feedback.entity.Feedback;
import com.mongs.member.domain.feedback.entity.FeedbackLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("피드백을 등록하면 피드백 로그도 함께 저장된다.")
    void addFeedback() {
        // given
        Long feedbackId = 1L;
        Long accountId = 2L;
        String deviceId = "test-deviceId";
        String code = "FB0000";
        String title = "피드백 제목";
        String content = "피드백 본문";
        LocalDateTime createdAt = LocalDateTime.now();

        Feedback feedback = Feedback.builder()
                .id(feedbackId)
                .accountId(accountId)
                .deviceId(deviceId)
                .code(code)
                .title(title)
                .content(content)
                .build();

        List<FeedbackLog> feedbackLogList = new ArrayList<>();
        for (int idx = 0; idx < 10; idx++) {
            feedbackLogList.add(FeedbackLog.builder()
                        .feedback(feedback)
                        .location("location-" + idx)
                        .message("message-" + idx)
                        .createdAt(createdAt)
                    .build());
        }

        // when
        Feedback saveFeedback = feedbackRepository.saveAndFlush(feedback.toBuilder()
                .feedbackLogList(feedbackLogList)
                .build());

        var expected = createdAt.plusSeconds(5);

        // then
        assertThat(saveFeedback.getId()).isEqualTo(feedbackId);
        assertThat(saveFeedback.getAccountId()).isEqualTo(accountId);
        assertThat(saveFeedback.getDeviceId()).isEqualTo(deviceId);
        assertThat(saveFeedback.getCode()).isEqualTo(code);
        assertThat(saveFeedback.getTitle()).isEqualTo(title);
        assertThat(saveFeedback.getContent()).isEqualTo(content);

        AtomicInteger idx = new AtomicInteger(0);
        saveFeedback.getFeedbackLogList().forEach(feedbackLog -> {
            int nowIdx = idx.getAndIncrement();
            assertThat(feedbackLog.getFeedback().getId()).isEqualTo(feedbackId);
            assertThat(feedbackLog.getLocation()).isEqualTo("location-" + nowIdx);
            assertThat(feedbackLog.getMessage()).isEqualTo("message-" + nowIdx);
            assertThat(feedbackLog.getCreatedAt()).isBefore(expected);
        });
    }
}
