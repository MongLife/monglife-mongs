package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.entity.MemberEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.MemberRepository;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreatePlayerVo;
import com.monglife.mongs.domain.member.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
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

    private static final Long ACCOUNT_ID = 1L;

    @Nested
    @DisplayName("플레이어 등록 단위 테스트")
    class CreatePlayerPort {

        @Test
        @DisplayName("플레이어를 등록에 성공하는 경우 플레이어 옵셔널 객체를 반환 한다.")
        void createPlayer() {
            // arrange
            int slotCount = 1;
            int starPoint = 100;
            CreatePlayerVo createPlayerVo = CreatePlayerVo.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            // act
            Optional<Player> playerOptional = memberPersistencePort.createPlayerPort(createPlayerVo);

            // assert
            assertTrue(playerOptional.isPresent());
            assertEquals(ACCOUNT_ID, playerOptional.get().getAccountId());
            assertEquals(slotCount, playerOptional.get().getSlotCount());
            assertEquals(starPoint, playerOptional.get().getStarPoint());
        }

        @Test
        @DisplayName("플레이어가 존재하는 경우 빈 옵셔널 객체를 반환 한다.")
        void createPlayerWhenExistPlayer() {
            // arrange
            int slotCount = 1;
            int starPoint = 100;
            CreatePlayerVo createPlayerVo = CreatePlayerVo.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            memberRepository.saveAndFlush(MemberEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build());

            // act
            Optional<Player> player = memberPersistencePort.createPlayerPort(createPlayerVo);

            // assert
            assertTrue(player.isEmpty());
        }
    }

    @Nested
    @DisplayName("플레이어 존재 여부 조회 단위 테스트")
    class IsExistsPlayerPort {

        @Test
        @DisplayName("플레이어 존재하는 경우 true를 반환 한다..")
        void isExistsPlayerWhenExistPlayer() {
            // arrange
            int slotCount = 1;
            int starPoint = 100;

            memberRepository.saveAndFlush(MemberEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build());

            // act
            boolean expected = memberPersistencePort.isExistsPlayerPort(ACCOUNT_ID);

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("플레이어 존재하는 경우 false를 반환 한다..")
        void isExistsPlayerWhenNotExistPlayer() {
            // act
            boolean expected = memberPersistencePort.isExistsPlayerPort(ACCOUNT_ID);

            // assert
            assertFalse(expected);
        }
    }

    @Nested
    @DisplayName("플레이어 조회 단위 테스트")
    class GetPlayerPort {

        @Test
        @DisplayName("플레이어를 조회 한다.")
        void getPlayer() {
            // arrange
            int slotCount = 1;
            int starPoint = 100;

            memberRepository.saveAndFlush(MemberEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build());

            // act
            Optional<Player> playerOptional = memberPersistencePort.getPlayerPort(ACCOUNT_ID);

            // assert
            assertTrue(playerOptional.isPresent());
            assertEquals(ACCOUNT_ID, playerOptional.get().getAccountId());
            assertEquals(slotCount, playerOptional.get().getSlotCount());
            assertEquals(starPoint, playerOptional.get().getStarPoint());
        }

        @Test
        @DisplayName("플레이어가 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getPlayerWhenNotExistPlayer() {
            // act
            Optional<Player> playerOptional = memberPersistencePort.getPlayerPort(ACCOUNT_ID);

            // assert
            assertTrue(playerOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("플레이어 수정 단위 테스트")
    class SavePlayerPort {

        @Test
        @DisplayName("플레이어 수정에 성공하는 경우 플레이어 옵셔널 객체를 반환 한다.")
        void savePlayer() {
            // arrange
            int slotCount = 5;
            int starPoint = 500;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            memberRepository.saveAndFlush(MemberEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(0)
                    .starPoint(0)
                    .build());

            // act
            Optional<Player> playerOptional = memberPersistencePort.savePlayerPort(player);

            // assert
            assertTrue(playerOptional.isPresent());
            assertEquals(ACCOUNT_ID, playerOptional.get().getAccountId());
            assertEquals(slotCount, playerOptional.get().getSlotCount());
            assertEquals(starPoint, playerOptional.get().getStarPoint());
        }

        @Test
        @DisplayName("플레이어가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void savePlayerWhenExistPlayer() {
            // arrange
            int slotCount = 5;
            int starPoint = 500;
            Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(slotCount)
                    .starPoint(starPoint)
                    .build();

            // act
            Optional<Player> playerOptional = memberPersistencePort.savePlayerPort(player);

            // assert
            assertTrue(playerOptional.isEmpty());
        }
    }
}