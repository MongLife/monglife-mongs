package com.mongs.common.service;

import com.mongs.common.code.TestFeedbackCode;
import com.mongs.common.code.TestFoodCode;
import com.mongs.common.code.TestMapCode;
import com.mongs.common.code.TestMongCode;
import com.mongs.common.controller.dto.response.FindFeedbackCodeResDto;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.repository.FeedbackCodeRepository;
import com.mongs.common.repository.FoodCodeRepository;
import com.mongs.common.repository.MapCodeRepository;
import com.mongs.common.repository.MongCodeRepository;
import com.mongs.core.entity.FeedbackCode;
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
import java.util.Map;
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
    @Mock
    private FeedbackCodeRepository feedbackCodeRepository;

    @Test
    @DisplayName("맵 코드 값을 반환한다.")
    void findMapCode() {
        // given
        String buildVersion = "1.0.0";

        List<MapCode> mapCodeList = Arrays.stream(TestMapCode.values())
                .map(testMapCode -> MapCode.builder()
                        .buildVersion(buildVersion)
                        .code(testMapCode.getCode())
                        .name(testMapCode.getName())
                        .build())
                .toList();

        when(mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion))
                .thenReturn(mapCodeList);

        // when
        List<FindMapCodeResDto> findMapCodeResDtoList = commonService.findMapCode(buildVersion);

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
                    assertThat(findMapCodeResDto.buildVersion()).isEqualTo(buildVersion);
                });
    }

    @Test
    @DisplayName("몽 코드 값을 반환한다.")
    void findMongCode() {
        // given
        String buildVersion = "1.0.0";

        List<MongCode> mongCodeList = Arrays.stream(TestMongCode.values())
                .map(testMongCode -> MongCode.builder()
                        .buildVersion(buildVersion)
                        .code(testMongCode.getCode())
                        .name(testMongCode.getName())
                        .build())
                .toList();

        when(mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion))
                .thenReturn(mongCodeList);

        // when
        List<FindMongCodeResDto> findMongCodeResDtoList = commonService.findMongCode(buildVersion);

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
                    assertThat(findMongCodeResDto.buildVersion()).isEqualTo(buildVersion);
                });
    }

    @Test
    @DisplayName("음식 코드 값을 반환한다.")
    void findFoodCode() {
        // given
        String buildVersion = "1.0.0";

        List<FoodCode> foodCodeList = Arrays.stream(TestFoodCode.values())
                .map(testFoodCode -> FoodCode.builder()
                        .buildVersion(buildVersion)
                        .name(testFoodCode.getName())
                        .code(testFoodCode.getCode())
                        .groupCode(testFoodCode.getGroupCode())
                        .price(testFoodCode.getPrice())
                        .addWeightValue(testFoodCode.getAddWeightValue())
                        .addStrengthValue(testFoodCode.getAddStrengthValue())
                        .addSatietyValue(testFoodCode.getAddSatietyValue())
                        .addHealthyValue(testFoodCode.getAddHealthyValue())
                        .addSleepValue(testFoodCode.getAddSleepValue())
                        .build())
                .toList();

        when(foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion))
                .thenReturn(foodCodeList);

        // when
        List<FindFoodCodeResDto> findFoodCodeResDtoList = commonService.findFoodCode(buildVersion);

        Set<String> expected1 =
                Arrays.stream(TestFoodCode.values())
                        .map(TestFoodCode::getName)
                        .collect(Collectors.toSet());
        Set<String> expected2 =
                Arrays.stream(TestFoodCode.values())
                        .map(TestFoodCode::getCode)
                        .collect(Collectors.toSet());
        Set<String> expected3 =
                Arrays.stream(TestFoodCode.values())
                        .map(TestFoodCode::getGroupCode)
                        .collect(Collectors.toSet());

        // then
        findFoodCodeResDtoList
                .forEach(findFoodCodeResDto -> {
                    assertThat(expected1.contains(findFoodCodeResDto.name())).isTrue();
                    assertThat(expected2.contains(findFoodCodeResDto.code())).isTrue();
                    assertThat(expected3.contains(findFoodCodeResDto.groupCode())).isTrue();
                    assertThat(findFoodCodeResDto.price()).isNotNull();
                    assertThat(findFoodCodeResDto.buildVersion()).isEqualTo(buildVersion);
                });
    }

    @Test
    @DisplayName("피드백 코드 값을 반환한다.")
    void findFeedbackCode() {
        // given
        String buildVersion = "1.0.0";

        List<FeedbackCode> feedbackCodeList = Arrays.stream(TestFeedbackCode.values())
                .map(testFeedbackCode -> FeedbackCode.builder()
                        .buildVersion(buildVersion)
                        .code(testFeedbackCode.getCode())
                        .groupCode(testFeedbackCode.getGroupCode())
                        .message(testFeedbackCode.getMessage())
                        .build())
                .toList();

        when(feedbackCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion))
                .thenReturn(feedbackCodeList);

        // when
        List<FindFeedbackCodeResDto> findFeedbackCodeResDtoList = commonService.findFeedbackCode(buildVersion);

        Set<String> expected1 =
                Arrays.stream(TestFeedbackCode.values())
                        .map(TestFeedbackCode::getCode)
                        .collect(Collectors.toSet());
        Set<String> expected2 =
                Arrays.stream(TestFeedbackCode.values())
                        .map(TestFeedbackCode::getGroupCode)
                        .collect(Collectors.toSet());
        Set<String> expected3 =
                Arrays.stream(TestFeedbackCode.values())
                        .map(TestFeedbackCode::getMessage)
                        .collect(Collectors.toSet());

        // then
        findFeedbackCodeResDtoList
                .forEach(findFeedbackCodeResDto -> {
                    assertThat(expected1.contains(findFeedbackCodeResDto.code())).isTrue();
                    assertThat(expected2.contains(findFeedbackCodeResDto.groupCode())).isTrue();
                    assertThat(expected3.contains(findFeedbackCodeResDto.message())).isTrue();
                    assertThat(findFeedbackCodeResDto.buildVersion()).isEqualTo(buildVersion);
                });
    }
}