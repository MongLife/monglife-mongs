package com.mongs.common.controller;

import com.mongs.common.code.TestFoodCode;
import com.mongs.common.code.TestMapCode;
import com.mongs.common.code.TestMongCode;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.service.CommonService;
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

    private final List<MapCode> mapCodeList =
            Arrays.stream(TestMapCode.values())
            .map(testMapCode -> MapCode.builder()
                    .code(testMapCode.getCode())
                    .name(testMapCode.getName())
                    .build())
            .toList();

    private final List<MongCode> mongCodeList =
            Arrays.stream(TestMongCode.values())
            .map(testMongCode -> MongCode.builder()
                    .code(testMongCode.getCode())
                    .name(testMongCode.getName())
                    .build())
            .toList();

    private final List<FoodCode> foodCodeList =
            Arrays.stream(TestFoodCode.values())
            .map(testFoodCode -> FoodCode.builder()
                    .code(testFoodCode.getCode())
                    .name(testFoodCode.getName())
                    .build())
            .toList();

    @Test
    @DisplayName("맵 코드 값 리스트를 반환한다.")
    void findMap() throws Exception {
        // given

        when(commonService.findMapCode())
                .thenReturn(FindMapCodeResDto.toList(mapCodeList));

        // when
        ResultActions resultActions = mockMvc.perform(get("/common/map")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int idx = 0; idx < mapCodeList.size(); idx++) {
            MapCode mapCode = mapCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.[" + idx + "].code").value(mapCode.code()));
            resultActions.andExpect(jsonPath("$.[" + idx + "].name").value(mapCode.name()));
        }
    }

    @Test
    @DisplayName("몽 코드 값 리스트를 반환한다.")
    void findMong() throws Exception {
        // given

        when(commonService.findMongCode())
                .thenReturn(FindMongCodeResDto.toList(mongCodeList));

        // when
        ResultActions resultActions = mockMvc.perform(get("/common/mong")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int idx = 0; idx < mongCodeList.size(); idx++) {
            MongCode mongCode = mongCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.[" + idx + "].code").value(mongCode.code()));
            resultActions.andExpect(jsonPath("$.[" + idx + "].name").value(mongCode.name()));
        }
    }

    @Test
    @DisplayName("음식 코드 값 리스트를 반환한다.")
    void findFood() throws Exception {
        // given

        when(commonService.findFoodCode())
                .thenReturn(FindFoodCodeResDto.toList(foodCodeList));

        // when
        ResultActions resultActions = mockMvc.perform(get("/common/food")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int idx = 0; idx < foodCodeList.size(); idx++) {
            FoodCode foodCode = foodCodeList.get(idx);
            resultActions.andExpect(jsonPath("$.[" + idx + "].code").value(foodCode.code()));
            resultActions.andExpect(jsonPath("$.[" + idx + "].name").value(foodCode.name()));
            resultActions.andExpect(jsonPath("$.[" + idx + "].groupCode").value(foodCode.groupCode()));
            resultActions.andExpect(jsonPath("$.[" + idx + "].point").value(foodCode.price()));
        }
    }
}