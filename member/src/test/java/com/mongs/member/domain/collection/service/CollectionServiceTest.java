package com.mongs.member.domain.collection.service;

import com.mongs.member.domain.collection.code.TestMapCode;
import com.mongs.member.domain.collection.code.TestMongCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import com.mongs.member.domain.collection.dto.response.FindMapCollectionResDto;
import com.mongs.member.domain.collection.dto.response.FindMongCollectionResDto;
import com.mongs.member.domain.collection.entity.MapCollection;
import com.mongs.member.domain.collection.entity.MongCollection;
import com.mongs.member.domain.collection.exception.CollectionErrorCode;
import com.mongs.member.domain.collection.exception.InvalidCodeException;
import com.mongs.member.domain.collection.repository.MapCodeRepository;
import com.mongs.member.domain.collection.repository.MapCollectionRepository;
import com.mongs.member.domain.collection.repository.MongCodeRepository;
import com.mongs.member.domain.collection.repository.MongCollectionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionServiceTest {
    @InjectMocks
    private CollectionService collectionService;
    @Mock
    private MongCodeRepository mongCodeRepository;
    @Mock
    private MapCodeRepository mapCodeRepository;
    @Mock
    private MapCollectionRepository mapCollectionRepository;
    @Mock
    private MongCollectionRepository mongCollectionRepository;

    private final MapCode testMapCode =  new MapCode(TestMapCode.MP000.getCode(), TestMapCode.MP000.getName(), 1L);
    private final MongCode testMongCode = new MongCode(TestMongCode.CH000.getCode(), TestMongCode.CH000.getName(), 1L);

    @Nested
    @DisplayName("조회 단위 테스트")
    class Find {
        @Test
        @DisplayName("회원 id 로 맵 컬렉션을 조회하면 맵 컬렉션 리스트를 반환한다.")
        void findMapCollection() {
            // given
            Long memberId = 1L;
            List<TestMapCode> mapCollectionCodeList = List.of(
                    TestMapCode.MP000,
                    TestMapCode.MP001,
                    TestMapCode.MP002
            );
            List<MapCollection> mapCollectionList = mapCollectionCodeList.stream()
                    .map(mapCode -> MapCollection.builder()
                            .id(1L)
                            .memberId(memberId)
                            .code(mapCode.getCode())
                            .build())
                    .toList();

            when(mapCodeRepository.findAll())
                    .thenReturn(Arrays.stream(TestMapCode.values())
                            .map(code -> MapCode.builder()
                                    .code(code.getCode())
                                    .name(code.getName())
                                    .build())
                            .toList());
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
            List<TestMongCode> mongCollectionCodeList = List.of(
                    TestMongCode.CH000,
                    TestMongCode.CH001,
                    TestMongCode.CH002
            );
            List<MongCollection> mongCollectionList = mongCollectionCodeList.stream()
                    .map(mongCode -> MongCollection.builder()
                            .id(1L)
                            .memberId(memberId)
                            .code(mongCode.getCode())
                            .build())
                    .toList();

            when(mongCodeRepository.findAll())
                    .thenReturn(Arrays.stream(TestMongCode.values())
                            .map(code -> MongCode.builder()
                                    .code(code.getCode())
                                    .name(code.getName())
                                    .build())
                            .toList());
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
    }

    @Nested
    @DisplayName("등록 단위 테스트")
    class Register {
        @Test
        @DisplayName("회원 id, 맵 코드로 맵 컬렉션을 등록하고, 등록된 값을 반환한다.")
        void registerMapCollection() {
            // given
            Long memberId = 1L;
            String mapCode = testMapCode.code();
            MapCollection mapCollection = MapCollection.builder()
                    .id(1L)
                    .memberId(memberId)
                    .code(mapCode)
                    .build();

            when(mapCodeRepository.findById(mapCode))
                    .thenReturn(Optional.of(testMapCode));
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
        @DisplayName("존재하지 않는 맵 코드로 등록하면 InvalidCodeException 이 발생한다.")
        void registerMapCollectionNotFoundMapCode() {
            // given
            Long memberId = 1L;
            String mapCode = "INVALID_CODE";

            when(mapCodeRepository.findById(mapCode))
                    .thenReturn(Optional.empty());

            // when
            Throwable e = catchThrowable(() -> collectionService.registerMapCollection(memberId, mapCode));

            var expected = ((InvalidCodeException) e).errorCode;
            var errorCode = CollectionErrorCode.INVALID_MAP_CODE;

            // then
            assertThat(e).isInstanceOf(InvalidCodeException.class);
            assertThat(expected.getHttpStatus()).isEqualTo(errorCode.getHttpStatus());
            assertThat(expected.getCode()).isEqualTo(errorCode.getCode());
            assertThat(expected.getMessage()).isEqualTo(errorCode.getMessage());
        }

        @Test
        @DisplayName("회원 id, 몽 코드로 몽 컬렉션을 등록하고, 등록된 값을 반환한다.")
        void registerMongCollection() {
            // given
            Long memberId = 1L;
            String mongCode = testMongCode.code();
            MongCollection mongCollection = MongCollection.builder()
                    .id(1L)
                    .memberId(memberId)
                    .code(mongCode)
                    .build();

            when(mongCodeRepository.findById(mongCode))
                    .thenReturn(Optional.of(testMongCode));
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
        @DisplayName("존재하지 않는 몽 코드로 등록하면 InvalidCodeException 이 발생한다.")
        void registerMongCollectionNotFoundMongCode() {
            // given
            Long memberId = 1L;
            String mongCode = "INVALID_CODE";

            when(mongCodeRepository.findById(mongCode))
                    .thenReturn(Optional.empty());

            // when
            Throwable e = catchThrowable(() -> collectionService.registerMongCollection(memberId, mongCode));

            var expected = ((InvalidCodeException) e).errorCode;
            var errorCode = CollectionErrorCode.INVALID_MONG_CODE;

            // then
            assertThat(e).isInstanceOf(InvalidCodeException.class);
            assertThat(expected.getHttpStatus()).isEqualTo(errorCode.getHttpStatus());
            assertThat(expected.getCode()).isEqualTo(errorCode.getCode());
            assertThat(expected.getMessage()).isEqualTo(errorCode.getMessage());
        }
    }

    @Nested
    @DisplayName("삭제 단위 테스트")
    class Remove {
        @Test
        @DisplayName("회원 id, 맵 코드로 맵 컬렉션을 삭제한다.")
        void removeMapCollection() {
            // given
            Long memberId = 1L;
            String mapCode = testMapCode.code();

            when(mapCodeRepository.findById(mapCode))
                    .thenReturn(Optional.of(testMapCode));
            doNothing().when(mapCollectionRepository).deleteByMemberIdAndCode(memberId, mapCode);

            // when
            Throwable e = catchThrowable(() -> collectionService.removeMapCollection(memberId, mapCode));

            // then
            assertThat(e).isNull();
        }

        @Test
        @DisplayName("존재하지 않는 맵 코드로 삭제하면 InvalidCodeException 이 발생한다.")
        void removeMapCollectionNotFoundMapCode() {
            // given
            Long memberId = 1L;
            String mapCode = "INVALID_CODE";

            when(mapCodeRepository.findById(mapCode))
                    .thenReturn(Optional.empty());

            // when
            Throwable e = catchThrowable(() -> collectionService.removeMapCollection(memberId, mapCode));

            var expected = ((InvalidCodeException) e).errorCode;
            var errorCode = CollectionErrorCode.INVALID_MAP_CODE;

            // then
            assertThat(e).isInstanceOf(InvalidCodeException.class);
            assertThat(expected.getHttpStatus()).isEqualTo(errorCode.getHttpStatus());
            assertThat(expected.getCode()).isEqualTo(errorCode.getCode());
            assertThat(expected.getMessage()).isEqualTo(errorCode.getMessage());
        }

        @Test
        @DisplayName("회원 id, 몽 코드로 몽 컬렉션을 삭제한다.")
        void removeMongCollection() {
            // given
            Long memberId = 1L;
            String mongCode = testMongCode.code();

            when(mongCodeRepository.findById(mongCode))
                    .thenReturn(Optional.of(testMongCode));
            doNothing().when(mongCollectionRepository).deleteByMemberIdAndCode(memberId, mongCode);

            // when
            Throwable e = catchThrowable(() -> collectionService.removeMongCollection(memberId, mongCode));

            // then
            assertThat(e).isNull();
        }

        @Test
        @DisplayName("존재하지 않는 몽 코드로 삭제하면 InvalidCodeException 이 발생한다.")
        void removeMongCollectionNotFoundMongCode() {
            // given
            Long memberId = 1L;
            String mongCode = "INVALID_CODE";

            when(mongCodeRepository.findById(mongCode))
                    .thenReturn(Optional.empty());

            // when
            Throwable e = catchThrowable(() -> collectionService.removeMongCollection(memberId, mongCode));

            var expected = ((InvalidCodeException) e).errorCode;
            var errorCode = CollectionErrorCode.INVALID_MONG_CODE;

            // then
            assertThat(e).isInstanceOf(InvalidCodeException.class);
            assertThat(expected.getHttpStatus()).isEqualTo(errorCode.getHttpStatus());
            assertThat(expected.getCode()).isEqualTo(errorCode.getCode());
            assertThat(expected.getMessage()).isEqualTo(errorCode.getMessage());
        }
    }
}
