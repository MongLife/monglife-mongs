package com.mongs.auth.repository;

import com.mongs.auth.entity.Member;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 id를 입력하지 않아도 id가 등록된다.")
    void autoMemberId() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        // when
        Member saveMember = memberRepository.save(member);

        // then
        Long memberId = saveMember.getId();
        assertThat(memberId).isNotNull();
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원 가입 일자를 입력하지 않아도 가입일자가 등록된다.")
    void autoCreatedAt() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        LocalDateTime expected = LocalDateTime.now().plusSeconds(1);

        // when
        Member saveMember = memberRepository.save(member);

        // then
        LocalDateTime createdAt = saveMember.getCreatedAt();
        assertThat(createdAt).isNotNull();
        assertThat(createdAt).isBeforeOrEqualTo(expected);
    }

    @Test
    @DisplayName("회원 정보를 수정하면 수정일자가 변경된다.")
    void autoUpdatedAt() {
        // given
        Member member = Member.builder()
                .email("test@gmail.com")
                .name("테스트 이름")
                .build();

        member = memberRepository.save(member);
        LocalDateTime createdAt = member.getCreatedAt();

        int delaySeconds = 5;
        Awaitility.await()
                .pollDelay(Duration.ofSeconds(delaySeconds))
                .until(() -> true);

        LocalDateTime expected1 = createdAt.plusSeconds(delaySeconds);
        LocalDateTime expected2 = LocalDateTime.now().plusSeconds(1);

        // when
        Member modifyMember = memberRepository.saveAndFlush(member.toBuilder().name("수정 테스트 이름").build());

        // then
        LocalDateTime updatedAt = modifyMember.getUpdatedAt();
        assertThat(updatedAt).isAfterOrEqualTo(expected1);
        assertThat(updatedAt).isBeforeOrEqualTo(expected2);
    }
}
