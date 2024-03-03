package com.mongs.auth.repository;

import com.mongs.auth.entity.Account;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("회원 id를 입력하지 않아도 id가 등록된다.")
    void autoMemberId() {
        // given
        Account account = Account.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        // when
        Account saveAccount = accountRepository.save(account);

        // then
        Long accountId = saveAccount.getId();
        assertThat(accountId).isNotNull();
        assertThat(accountId).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원 가입 일자가 자동으로 등록된다.")
    void autoCreatedAt() {
        // given
        Account account = Account.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        LocalDateTime expected = LocalDateTime.now().plusSeconds(1);

        // when
        Account saveAccount = accountRepository.save(account);

        // then
        LocalDateTime createdAt = saveAccount.getCreatedAt();
        assertThat(createdAt).isNotNull();
        assertThat(createdAt).isBeforeOrEqualTo(expected);
    }

    @Test
    @DisplayName("회원 정보를 수정 시, 수정 일자가 변경 된다.")
    void autoUpdatedAt() {
        // given
        Account account = Account.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        account = accountRepository.save(account);
        LocalDateTime createdAt = account.getCreatedAt();

        int delaySeconds = 5;
        Awaitility.await()
                .pollDelay(Duration.ofSeconds(delaySeconds))
                .until(() -> true);

        LocalDateTime expected1 = createdAt.plusSeconds(delaySeconds);
        LocalDateTime expected2 = LocalDateTime.now().plusSeconds(1);

        // when
        Account modifyAccount = accountRepository.saveAndFlush(account.toBuilder().name("수정 테스트 이름").build());

        // then
        LocalDateTime updatedAt = modifyAccount.getUpdatedAt();
        assertThat(updatedAt).isAfterOrEqualTo(expected1);
        assertThat(updatedAt).isBeforeOrEqualTo(expected2);
    }
}
