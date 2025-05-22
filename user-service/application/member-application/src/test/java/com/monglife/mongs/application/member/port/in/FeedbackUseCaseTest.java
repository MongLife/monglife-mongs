package com.monglife.mongs.application.member.port.in;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.application.member.port.exception.InvalidCreateFeedbackException;
import com.monglife.mongs.application.member.port.in.command.CreateFeedbackCommand;
import com.monglife.mongs.application.member.port.in.service.FeedbackService;
import com.monglife.mongs.application.member.port.out.FeedbackPersistencePort;
import com.monglife.mongs.domain.member.model.Feedback;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackUseCaseTest {

    private final FeedbackPersistencePort feedbackPersistencePort;

    private final FeedbackUseCase feedbackUseCase;

    public FeedbackUseCaseTest() {
        this.feedbackPersistencePort = Mockito.mock(FeedbackPersistencePort.class);
        this.feedbackUseCase = new FeedbackService(feedbackPersistencePort);
    }

    private static final Long ACCOUNT_ID = 1L;
    private static final String DEVICE_ID = CommonUtil.randomId();
    private static final String DEVICE_NAME = "TEST-DEVICE-NAME";


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
                    .accountId(ACCOUNT_ID)
                    .deviceId(DEVICE_ID)
                    .deviceName(DEVICE_NAME)
                    .title(title)
                    .content(content)
                    .build();

            Mockito.when(feedbackPersistencePort.createFeedbackPort(Mockito.any())).thenReturn(Optional.of(feedback));

            // act
            CreateFeedbackCommand command = CreateFeedbackCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .deviceId(DEVICE_ID)
                    .deviceName(DEVICE_NAME)
                    .title(title)
                    .content(content)
                    .build();

            feedbackUseCase.createFeedbackUseCase(command);

            // assert
            Mockito.verify(feedbackPersistencePort).createFeedbackPort(Mockito.any());
        }

        @Test
        @DisplayName("오류 신고 등록에 실패하는 경우 예외가 발생 한다.")
        void createFeedbackFail() {
            // arrange
            String title = "TEST-TITLE";
            String content = "TEST-CONTENT";

            Mockito.when(feedbackPersistencePort.createFeedbackPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateFeedbackCommand command = CreateFeedbackCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .deviceId(DEVICE_ID)
                    .deviceName(DEVICE_NAME)
                    .title(title)
                    .content(content)
                    .build();

            assertThrows(InvalidCreateFeedbackException.class, () -> feedbackUseCase.createFeedbackUseCase(command));
        }
    }
}