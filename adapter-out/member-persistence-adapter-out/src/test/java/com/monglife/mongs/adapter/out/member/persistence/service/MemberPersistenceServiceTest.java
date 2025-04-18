package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.repository.MemberRepository;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutMemberPersistenceConfig.class, MemberDataSourceConfig.class, HibernateAutoConfig.class })
class MemberPersistenceServiceTest {

    private final MemberPersistencePort memberPersistencePort;

    private final MemberRepository memberRepository;

    @Autowired
    public MemberPersistenceServiceTest(MemberPersistencePort memberPersistencePort, MemberRepository memberRepository) {
        this.memberPersistencePort = memberPersistencePort;
        this.memberRepository = memberRepository;
    }

    @Nested
    @DisplayName("플레이어 등록 단위 테스트")
    class CreatePlayerPort {

        @Test
        @DisplayName("플레이어를 등록 한다.")
        void createPlayer() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("플레이어 존재 여부 조회 단위 테스트")
    class IsExistsPlayerPort {

        @Test
        @DisplayName("플레이어 존재 여부를 조회 한다.")
        void isExistsPlayer() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("플레이어 조회 단위 테스트")
    class GetPlayerPort {

        @Test
        @DisplayName("플레이어를 조회 한다.")
        void getPlayer() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("플레이어 수정 단위 테스트")
    class SavePlayerPort {

        @Test
        @DisplayName("플레이어 정보를 수정 한다.")
        void savePlayer() {
            // arrange

            // act

            // assert

        }
    }
}