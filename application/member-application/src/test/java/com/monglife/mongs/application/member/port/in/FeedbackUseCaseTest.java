package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;
import com.monglife.mongs.application.member.port.in.service.FeedbackService;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.domain.model.Feedback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FeedbackUseCaseTest {

    private final MemberPersistencePort memberPersistencePort;

    private final FeedbackService feedbackService;

    public FeedbackUseCaseTest() {
        this.memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
        this.feedbackService = new FeedbackService(memberPersistencePort);
    }

    private static final Long accountId = 1L;
    private static final String deviceId = "TEST-DEVICE-ID";
    private static final String deviceName = "TEST-DEVICE-NAME";


    @Nested
    @DisplayName("오류 신고 등록 단위 테스트")
    class CreateFeedbackUseCase {

        @Test
        @DisplayName("오류 신고를 등록 한다.")
        void createFeedback() {
            // arrange
            String title = "TEST-TITLE";
            String content = "TEST-CONTENT";
            Feedback feedback = Feedback.builder()
                    .feedbackId(1L)
                    .accountId(accountId)
                    .deviceId(deviceId)
                    .deviceName(deviceName)
                    .title(title)
                    .content(content)
                    .build();

            Mockito.when(memberPersistencePort.createFeedbackPort(Mockito.any())).thenReturn(feedback);

            // act
            feedbackService.createFeedbackUseCase(CreateFeedbackCommand.builder()
                            .accountId(accountId)
                            .deviceId(deviceId)
                            .deviceName(deviceName)
                            .title(title)
                            .content(content)
                    .build());

            // assert
            Mockito.verify(memberPersistencePort).createFeedbackPort(Mockito.any());
        }
    }
}