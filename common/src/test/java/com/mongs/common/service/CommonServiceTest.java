package com.mongs.common.service;

import com.mongs.common.code.TestFoodCode;
import com.mongs.common.code.TestMapCode;
import com.mongs.common.code.TestMongCode;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.repository.FoodCodeRepository;
import com.mongs.common.repository.MapCodeRepository;
import com.mongs.common.repository.MongCodeRepository;
import com.mongs.core.entity.FoodCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CommonServiceTest {
    @InjectMocks
    private CommonService commonService;
    @Mock
    private MapCodeRepository mapCodeRepository;
    @Mock
    private MongCodeRepository mongCodeRepository;
    @Mock
    private FoodCodeRepository foodCodeRepository;

    @Test
    @DisplayName("맵 코드 값을 반환한다.")
    void findMapCode() {
        // given

        List<MapCode> mapCodeList = Arrays.stream(TestMapCode.values())
                .map(testMapCode -> MapCode.builder()
                        .code(testMapCode.getCode())
                        .name(testMapCode.getName())
                        .build())
                .toList();

        when(mapCodeRepository.findAll())
                .thenReturn(mapCodeList);

        // when
        List<FindMapCodeResDto> findMapCodeResDtoList = commonService.findMapCode();

        Set<String> expected1 =
                Arrays.stream(TestMapCode.values())
                        .map(TestMapCode::getCode)
                        .collect(Collectors.toSet());
        Set<String> expected2 =
                Arrays.stream(TestMapCode.values())
                        .map(TestMapCode::getName)
                        .collect(Collectors.toSet());

        // then
        findMapCodeResDtoList
                .forEach(findMapCodeResDto -> {
                    assertThat(expected1.contains(findMapCodeResDto.code())).isTrue();
                    assertThat(expected2.contains(findMapCodeResDto.name())).isTrue();
                });
    }

    @Test
    @DisplayName("몽 코드 값을 반환한다.")
    void findMongCode() {
        // given
        Long version = 1L;

        List<MongCode> mongCodeList = Arrays.stream(TestMongCode.values())
                .map(testMongCode -> MongCode.builder()
                        .code(testMongCode.getCode())
                        .name(testMongCode.getName())
                        .build())
                .toList();

        when(mongCodeRepository.findAll())
                .thenReturn(mongCodeList);

        // when
        List<FindMongCodeResDto> findMongCodeResDtoList = commonService.findMongCode();

        Set<String> expected1 =
                Arrays.stream(TestMongCode.values())
                        .map(TestMongCode::getCode)
                        .collect(Collectors.toSet());
        Set<String> expected2 =
                Arrays.stream(TestMongCode.values())
                        .map(TestMongCode::getName)
                        .collect(Collectors.toSet());

        // then
        findMongCodeResDtoList
                .forEach(findMongCodeResDto -> {
                    assertThat(expected1.contains(findMongCodeResDto.code())).isTrue();
                    assertThat(expected2.contains(findMongCodeResDto.name())).isTrue();
                });
    }

    @Test
    @DisplayName("음식 코드 값을 반환한다.")
    void findFoodCode() {
        // given
        Long version = 1L;

        List<FoodCode> foodCodeList = Arrays.stream(TestFoodCode.values())
                .map(testFoodCode -> FoodCode.builder()
                        .code(testFoodCode.getCode())
                        .name(testFoodCode.getName())
                        .build())
                .toList();

        when(foodCodeRepository.findAll())
                .thenReturn(foodCodeList);

        // when
        List<FindFoodCodeResDto> findFoodCodeResDtoList = commonService.findFoodCode();

        Set<String> expected1 =
                Arrays.stream(TestFoodCode.values())
                        .map(TestFoodCode::getCode)
                        .collect(Collectors.toSet());
        Set<String> expected2 =
                Arrays.stream(TestFoodCode.values())
                        .map(TestFoodCode::getName)
                        .collect(Collectors.toSet());

        // then
        findFoodCodeResDtoList
                .forEach(findFoodCodeResDto -> {
                    assertThat(expected1.contains(findFoodCodeResDto.code())).isTrue();
                    assertThat(expected2.contains(findFoodCodeResDto.name())).isTrue();
                });
    }
}