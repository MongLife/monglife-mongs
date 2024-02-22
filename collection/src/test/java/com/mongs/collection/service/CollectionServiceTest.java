package com.mongs.collection.service;

import com.mongs.collection.code.TestMapCode;
import com.mongs.collection.dto.response.FindMapCollectionResDto;
import com.mongs.collection.entity.MapCollection;
import com.mongs.collection.repository.MapCollectionRepository;
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
}
