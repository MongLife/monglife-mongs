package com.mongs.common.controller;

import com.mongs.common.code.TestFeedbackCode;
import com.mongs.common.code.TestFoodCode;
import com.mongs.common.code.TestMapCode;
import com.mongs.common.code.TestMongCode;
import com.mongs.common.controller.dto.response.FindFeedbackCodeResDto;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.service.CommonService;
import com.mongs.core.entity.FeedbackCode;
import com.mongs.core.entity.FoodCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonService commonService;

    private final String buildVersion = "1.0.0";

    private final List<MapCode> mapCodeList =
            Arrays.stream(TestMapCode.values())
            .map(testMapCode -> MapCode.builder()
                    .buildVersion(buildVersion)
                    .code(testMapCode.getCode())
                    .name(testMapCode.getName())
                    .build())
            .toList();

    private final List<MongCode> mongCodeList =
            Arrays.stream(TestMongCode.values())
            .map(testMongCode -> MongCode.builder()
                    .buildVersion(buildVersion)
                    .code(testMongCode.getCode())
                    .name(testMongCode.getName())
                    .build())
            .toList();

    private final List<FoodCode> foodCodeList =
            Arrays.stream(TestFoodCode.values())
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

    private final List<FeedbackCode> feedbackCodeList =
            Arrays.stream(TestFeedbackCode.values())
                    .map(testFeedbackCode -> FeedbackCode.builder()
                            .buildVersion(buildVersion)
                            .code(testFeedbackCode.getCode())
                            .groupCode(testFeedbackCode.getGroupCode())
                            .message(testFeedbackCode.getMessage())
                            .build())
                    .toList();


    @Test
    @DisplayName("코드 값 리스트를 반환한다.")
    void findCode() throws Exception {
        // given
        when(commonService.findMapCode())
                .thenReturn(FindMapCodeResDto.toList(mapCodeList));
        when(commonService.findMongCode())
                .thenReturn(FindMongCodeResDto.toList(mongCodeList));
        when(commonService.findFoodCode())
                .thenReturn(FindFoodCodeResDto.toList(foodCodeList));
        when(commonService.findFeedbackCode())
                .thenReturn(FindFeedbackCodeResDto.toList(feedbackCodeList));

        // when
        ResultActions resultActions = mockMvc.perform(get("/common/code")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int idx = 0; idx < mapCodeList.size(); idx++) {
            MapCode mapCode = mapCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.mapCodeList.[" + idx + "].code").value(mapCode.code()));
            resultActions.andExpect(jsonPath("$.mapCodeList.[" + idx + "].name").value(mapCode.name()));
            resultActions.andExpect(jsonPath("$.mapCodeList.[" + idx + "].buildVersion").value(buildVersion));
        }

        for (int idx = 0; idx < mongCodeList.size(); idx++) {
            MongCode mongCode = mongCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.mongCodeList.[" + idx + "].code").value(mongCode.code()));
            resultActions.andExpect(jsonPath("$.mongCodeList.[" + idx + "].name").value(mongCode.name()));
            resultActions.andExpect(jsonPath("$.mongCodeList.[" + idx + "].buildVersion").value(buildVersion));
        }

        for (int idx = 0; idx < foodCodeList.size(); idx++) {
            FoodCode foodCode = foodCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.foodCodeList.[" + idx + "].code").value(foodCode.code()));
            resultActions.andExpect(jsonPath("$.foodCodeList.[" + idx + "].name").value(foodCode.name()));
            resultActions.andExpect(jsonPath("$.foodCodeList.[" + idx + "].groupCode").value(foodCode.groupCode()));
            resultActions.andExpect(jsonPath("$.foodCodeList.[" + idx + "].price").value(foodCode.price()));
            resultActions.andExpect(jsonPath("$.foodCodeList.[" + idx + "].buildVersion").value(buildVersion));
        }

        for (int idx = 0; idx < feedbackCodeList.size(); idx++) {
            FeedbackCode feedbackCode = feedbackCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.feedbackCodeList.[" + idx + "].code").value(feedbackCode.code()));
            resultActions.andExpect(jsonPath("$.feedbackCodeList.[" + idx + "].groupCode").value(feedbackCode.groupCode()));
            resultActions.andExpect(jsonPath("$.feedbackCodeList.[" + idx + "].message").value(feedbackCode.message()));
            resultActions.andExpect(jsonPath("$.feedbackCodeList.[" + idx + "].buildVersion").value(buildVersion));
        }
    }
}