package com.mongs.collection.service;

import com.mongs.collection.code.TestMapCode;
import com.mongs.collection.code.TestMongCode;
import com.mongs.collection.dto.response.FindMapCollectionResDto;
import com.mongs.collection.dto.response.FindMongCollectionResDto;
import com.mongs.collection.entity.MapCollection;
import com.mongs.collection.entity.MongCollection;
import com.mongs.collection.repository.MapCollectionRepository;
import com.mongs.collection.repository.MongCollectionRepository;
import com.mongs.core.code.CodeRepository;
import com.mongs.core.code.GroupCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionServiceTest {
    @InjectMocks
    private CollectionService collectionService;
    @Mock
    private CodeRepository codeRepository;
    @Mock
    private MapCollectionRepository mapCollectionRepository;
    @Mock
    private MongCollectionRepository mongCollectionRepository;

    @Test
    @DisplayName("회원 id 로 맵 컬렉션을 조회하면 맵 컬렉션 리스트를 반환한다.")
    void findMapCollection() {
        // given
        Long memberId = 1L;
        String groupCode = GroupCode.MAP.getGroupCode();
        List<TestMapCode> mapCollectionCodeList = List.of(
                TestMapCode.MP000,
                TestMapCode.MP001,
                TestMapCode.MP002
        );
        List<MapCollection> mapCollectionList = mapCollectionCodeList.stream()
                        .map(mapCode -> MapCollection.builder()
                                .id(1L)
                                .memberId(memberId)
                                .groupCode(groupCode)
                                .code(mapCode.getCode())
                                .build())
                        .toList();

        when(codeRepository.findByGroupCode(groupCode))
                .thenReturn(List.of(TestMapCode.values()));
        when(mapCollectionRepository.findByMemberId(memberId))
                .thenReturn(mapCollectionList);

        // when
        List<FindMapCollectionResDto> findMapCollectionResDtoList = collectionService.findMapCollection(memberId);

        // then
        assertThat(findMapCollectionResDtoList).isNotNull();
        assertThat(findMapCollectionResDtoList).isNotEmpty();
        findMapCollectionResDtoList.forEach(findMapCollectionResDto -> {
            String code = findMapCollectionResDto.code();

            if (mapCollectionCodeList.contains(TestMapCode.valueOf(code))) {
                assertThat(findMapCollectionResDto.disable()).isFalse();
            } else {
                assertThat(findMapCollectionResDto.disable()).isTrue();
            }
        });
    }

    @Test
    @DisplayName("회원 id 로 몽 컬렉션을 조회하면 몽 컬렉션 리스트를 반환한다.")
    void findMongCollection() {
        // given
        Long memberId = 1L;
        String groupCode = GroupCode.MONG.getGroupCode();
        List<TestMongCode> mongCollectionCodeList = List.of(
                TestMongCode.CH000,
                TestMongCode.CH001,
                TestMongCode.CH002
        );
        List<MongCollection> mongCollectionList = mongCollectionCodeList.stream()
                .map(mongCode -> MongCollection.builder()
                        .id(1L)
                        .memberId(memberId)
                        .groupCode(groupCode)
                        .code(mongCode.getCode())
                        .build())
                .toList();

        when(codeRepository.findByGroupCode(groupCode))
                .thenReturn(List.of(TestMongCode.values()));
        when(mongCollectionRepository.findByMemberId(memberId))
                .thenReturn(mongCollectionList);

        // when
        List<FindMongCollectionResDto> findMongCollectionResDtoList = collectionService.findMongCollection(memberId);

        // then
        assertThat(findMongCollectionResDtoList).isNotNull();
        assertThat(findMongCollectionResDtoList).isNotEmpty();
        findMongCollectionResDtoList.forEach(findMongCollectionResDto -> {
            String code = findMongCollectionResDto.code();

            if (mongCollectionCodeList.contains(TestMongCode.valueOf(code))) {
                assertThat(findMongCollectionResDto.disable()).isFalse();
            } else {
                assertThat(findMongCollectionResDto.disable()).isTrue();
            }
        });
    }

    @Test
    @DisplayName("회원 id, 맵 코드로 맵 컬렉션을 등록하고, 등록된 값을 반환한다.")
    void registerMapCollection() {
        // given
        Long memberId = 1L;
        String groupCode = GroupCode.MAP.getGroupCode();
        String mapCode = TestMapCode.MP000.getCode();
        MapCollection mapCollection = MapCollection.builder()
                .id(1L)
                .memberId(memberId)
                .groupCode(groupCode)
                .code(mapCode)
                .build();

        when(mapCollectionRepository.save(any()))
                .thenReturn(mapCollection);

        // when
        var registerMapCollectionResDto = collectionService.registerMapCollection(memberId, mapCode);

        // then
        assertThat(registerMapCollectionResDto).isNotNull();
        assertThat(registerMapCollectionResDto.memberId()).isEqualTo(memberId);
        assertThat(registerMapCollectionResDto.code()).isEqualTo(mapCode);
    }

    @Test
    @DisplayName("회원 id, 몽 코드로 몽 컬렉션을 등록하고, 등록된 값을 반환한다.")
    void registerMongCollection() {
        // given
        Long memberId = 1L;
        String groupCode = GroupCode.MONG.getGroupCode();
        String mongCode = TestMongCode.CH000.getCode();
        MongCollection mongCollection = MongCollection.builder()
                .id(1L)
                .memberId(memberId)
                .groupCode(groupCode)
                .code(mongCode)
                .build();

        when(mongCollectionRepository.save(any()))
                .thenReturn(mongCollection);

        // when
        var registerMongCollectionResDto = collectionService.registerMongCollection(memberId, mongCode);

        // then
        assertThat(registerMongCollectionResDto).isNotNull();
        assertThat(registerMongCollectionResDto.memberId()).isEqualTo(memberId);
        assertThat(registerMongCollectionResDto.code()).isEqualTo(mongCode);
    }

    @Test
    @DisplayName("회원 id, 맵 코드로 맵 컬렉션을 삭제한다.")
    void removeMapCollection() {
        // given
        Long memberId = 1L;
        String mapCode = TestMapCode.MP000.getCode();

        doNothing().when(mapCollectionRepository).deleteByMemberIdAndCode(memberId, mapCode);

        // when
        Throwable e = catchThrowable(() -> collectionService.removeMapCollection(memberId, mapCode));

        // then
        assertThat(e).isNull();
    }

    @Test
    @DisplayName("회원 id, 몽 코드로 몽 컬렉션을 삭제한다.")
    void removeMongCollection() {
        // given
        Long memberId = 1L;
        String mongCode = TestMongCode.CH000.getCode();

        doNothing().when(mongCollectionRepository).deleteByMemberIdAndCode(memberId, mongCode);

        // when
        Throwable e = catchThrowable(() -> collectionService.removeMongCollection(memberId, mongCode));

        // then
        assertThat(e).isNull();
    }
}
