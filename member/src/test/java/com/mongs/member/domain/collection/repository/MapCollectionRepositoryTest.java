package com.mongs.member.domain.collection.repository;

import com.mongs.member.domain.collection.code.TestMapCode;
import com.mongs.member.domain.collection.entity.MapCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MapCollectionRepositoryTest {
    @Autowired
    private MapCollectionRepository mapCollectionRepository;

    @Test
    @DisplayName("맵 컬렉션 id를 입력하지 않아도 id가 등록된다.")
    void autoAccountId() {
        // given
        Long accountId = 1L;
        String code = TestMapCode.MP000.getCode();

        MapCollection mapCollection = MapCollection.builder()
                .accountId(accountId)
                .code(code)
                .build();

        // when
        MapCollection saveMapCollection = mapCollectionRepository.save(mapCollection);

        // then
        Long mapCollectionId = saveMapCollection.getId();
        assertThat(mapCollectionId).isNotNull();
    }

    @Test
    @DisplayName("맵 컬렉션 등록 일자가 자동으로 등록된다.")
    void autoCreatedAt() {
        // given
        Long accountId = 1L;
        String code = TestMapCode.MP000.getCode();

        MapCollection mapCollection = MapCollection.builder()
                .accountId(accountId)
                .code(code)
                .build();

        LocalDateTime expected = LocalDateTime.now().plusSeconds(1);

        // when
        MapCollection saveMapCollection = mapCollectionRepository.save(mapCollection);

        // then
        LocalDateTime createdAt = saveMapCollection.getCreatedAt();
        assertThat(createdAt).isNotNull();
        assertThat(createdAt).isBeforeOrEqualTo(expected);
    }

    @Test
    @DisplayName("회원 id 를 기준으로 조회하면 회원의 맵 컬렉션 목록을 반환한다.")
    void findByAccountId() {
        // given
        Long accountId = 1L;

        for (long codeNumber = 0; codeNumber < 10; codeNumber++) {
            mapCollectionRepository.save(MapCollection.builder()
                    .accountId(accountId)
                    .code(String.format("MP%03d", codeNumber))
                    .build());
        }

        // when
        List<MapCollection> mapCollectionList = mapCollectionRepository.findByAccountId(accountId);

        // then
        assertThat(mapCollectionList).isNotEmpty();

        AtomicInteger codeNumber = new AtomicInteger();
        mapCollectionList.forEach(mapCollection -> {
            assertThat(mapCollection).isNotNull();
            assertThat(mapCollection.getAccountId()).isEqualTo(accountId);
            assertThat(mapCollection.getCode()).isEqualTo(String.format("MP%03d", codeNumber.getAndIncrement()));
        });
    }

    @Test
    @DisplayName("회원 id, 맵 코드를 기준으로 맵 컬렉션을 삭제한다.")
    void deleteByAccountIdAndMapCode() {
        // given
        Long accountId = 1L;
        String code = TestMapCode.MP000.getCode();

        MapCollection mapCollection = MapCollection.builder()
                .accountId(accountId)
                .code(code)
                .build();
        MapCollection saveMapCollection = mapCollectionRepository.save(mapCollection);

        // when
        mapCollectionRepository.deleteByAccountIdAndCode(accountId, code);

        Optional<MapCollection> expected  = mapCollectionRepository.findById(saveMapCollection.getId());

        // then
        assertThat(expected.isPresent()).isFalse();
    }
}