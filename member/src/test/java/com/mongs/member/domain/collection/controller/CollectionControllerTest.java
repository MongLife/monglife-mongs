package com.mongs.member.domain.collection.controller;

import com.mongs.member.domain.collection.code.TestMapCode;
import com.mongs.member.domain.collection.code.TestMongCode;
import com.mongs.member.domain.collection.dto.request.RegisterMapCollectionReqDto;
import com.mongs.member.domain.collection.dto.request.RegisterMongCollectionReqDto;
import com.mongs.member.domain.collection.dto.request.RemoveMapCollectionReqDto;
import com.mongs.member.domain.collection.dto.request.RemoveMongCollectionReqDto;
import com.mongs.member.domain.collection.dto.response.FindMapCollectionResDto;
import com.mongs.member.domain.collection.dto.response.FindMongCollectionResDto;
import com.mongs.member.domain.collection.dto.response.RegisterMapCollectionResDto;
import com.mongs.member.domain.collection.dto.response.RegisterMongCollectionResDto;
import com.mongs.member.domain.collection.exception.CollectionErrorCode;
import com.mongs.member.domain.collection.exception.InvalidCodeException;
import com.mongs.member.domain.collection.security.WithMockPassportDetail;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import com.mongs.core.error.ErrorCode;
import com.mongs.member.domain.collection.service.CollectionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CollectionControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CollectionService collectionService;

    private final MapCode testMapCode =  new MapCode(TestMapCode.MP000.getCode(), TestMapCode.MP000.getName(), 1L);
    private final MongCode testMongCode = new MongCode(TestMongCode.CH000.getCode(), TestMongCode.CH000.getName(), 1L);

    @Nested
    @DisplayName("조회 단위 테스트")
    @WithMockPassportDetail
    class Find {

        private final List<MapCode> mapCodeList =
                Arrays.stream(TestMapCode.values())
                        .map(mapCode -> new MapCode(mapCode.getCode(), mapCode.getName(), 1L))
                        .toList();
        private final List<MongCode> mongCodeList =
                Arrays.stream(TestMongCode.values())
                        .map(mongCode -> new MongCode(mongCode.getCode(), mongCode.getName(), 1L))
                        .toList();

        @Test
        @DisplayName("맵 컬렉션을 조회하면 맵 컬렉션 리스트를 반환한다.")
        void findMapCollection() throws Exception {
            // given
            Long memberId = 1L;
            List<MapCode> enable = List.of(
                    new MapCode(TestMapCode.MP000.getCode(), TestMapCode.MP000.getName(), 1L),
                    new MapCode(TestMapCode.MP001.getCode(), TestMapCode.MP001.getName(), 1L),
                    new MapCode(TestMapCode.MP002.getCode(), TestMapCode.MP002.getName(), 1L)
            );
            List<String> enableList = enable.stream().map(MapCode::code).toList();
            List<MapCode> disable = mapCodeList.stream()
                    .filter(mapCode -> !enableList.contains(mapCode.code()))
                    .toList();

            List<FindMapCollectionResDto> findMapCollectionResDtoList =
                    FindMapCollectionResDto.toList(enable, disable);

            when(collectionService.findMapCollection(memberId))
                    .thenReturn(findMapCollectionResDtoList);

            // when
            ResultActions resultActions = mockMvc.perform(get("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            for (int idx = 0; idx < enable.size(); idx++) {
                MapCode mapCode = enable.get(idx);
                resultActions
                        .andExpect(jsonPath("$["+idx+"].code").value(mapCode.code()))
                        .andExpect(jsonPath("$["+idx+"].disable").value(false));
            }
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            verify(collectionService, times(1)).findMapCollection(memberIdCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
        }

        @Test
        @DisplayName("몽 컬렉션을 조회하면 몽 컬렉션 리스트를 반환한다.")
        void findMongCollection() throws Exception {
            // given
            Long memberId = 1L;
            List<MongCode> enable = List.of(
                    new MongCode(TestMongCode.CH000.getCode(), TestMongCode.CH000.getName(), 1L),
                    new MongCode(TestMongCode.CH001.getCode(), TestMongCode.CH001.getName(), 1L),
                    new MongCode(TestMongCode.CH002.getCode(), TestMongCode.CH002.getName(), 1L)
            );
            List<String> enableList = enable.stream().map(MongCode::code).toList();
            List<MongCode> disable = mongCodeList.stream()
                    .filter(mongCode -> !enableList.contains(mongCode.code()))
                    .toList();

            List<FindMongCollectionResDto> findMongCollectionResDtoList =
                    FindMongCollectionResDto.toList(enable, disable);

            when(collectionService.findMongCollection(memberId))
                    .thenReturn(findMongCollectionResDtoList);

            // when
            ResultActions resultActions = mockMvc.perform(get("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            for (int idx = 0; idx < enable.size(); idx++) {
                MongCode mongCode = enable.get(idx);
                resultActions
                        .andExpect(jsonPath("$["+idx+"].code").value(mongCode.code()))
                        .andExpect(jsonPath("$["+idx+"].disable").value(false));
            }
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            verify(collectionService, times(1)).findMongCollection(memberIdCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
        }
    }

    @Nested
    @DisplayName("등록 단위 테스트")
    @WithMockPassportDetail
    class Register {
        @Test
        @DisplayName("맵 코드로 맵 컬렉션을 등록한다.")
        void registerMapCollection() throws Exception {
            // given
            Long memberId = 1L;
            String mapCode = testMapCode.code();
            RegisterMapCollectionResDto registerMapCollectionResDto =
                    RegisterMapCollectionResDto.builder()
                            .memberId(memberId)
                            .code(mapCode)
                            .createdAt(LocalDateTime.now())
                            .build();

            when(collectionService.registerMapCollection(memberId, mapCode))
                    .thenReturn(registerMapCollectionResDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMapCollectionReqDto(mapCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.memberId").value(1L))
                    .andExpect(jsonPath("$.code").value(mapCode))
                    .andExpect(jsonPath("$.createdAt").exists());
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mapCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).registerMapCollection(memberIdCaptor.capture(), mapCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMapCode = mapCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMapCode).isEqualTo(mapCode);
        }

        @Test
        @DisplayName("맵 코드 없이 맵 컬렉션을 등록하면 Invalid Parameter 에러 메시지를 반환한다.")
        void registerMapCollectionNotFoundMapCode() throws Exception {
            // given
            ErrorCode errorCode = CollectionErrorCode.INVALID_PARAMETER;

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMapCollectionReqDto(null))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }

        @Test
        @DisplayName("맵 코드가 아닌 코드로 맵 컬렉션을 등록하면 INVALID_MAP_CODE 에러 코드를 반환한다.")
        void registerMapCollectionInvalidMapCode() throws Exception {
            // given
            Long memberId = 1L;
            String mapCode = "INVALID_CODE";
            ErrorCode errorCode = CollectionErrorCode.INVALID_MAP_CODE;

            when(collectionService.registerMapCollection(memberId, mapCode))
                    .thenThrow(new InvalidCodeException(errorCode));

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMapCollectionReqDto(mapCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }

        @Test
        @DisplayName("몽 코드로 몽 컬렉션을 등록한다.")
        void registerMongCollection() throws Exception {
            // given
            Long memberId = 1L;
            String mongCode = testMongCode.code();
            RegisterMongCollectionResDto registerMongCollectionResDto =
                    RegisterMongCollectionResDto.builder()
                            .memberId(memberId)
                            .code(mongCode)
                            .createdAt(LocalDateTime.now())
                            .build();

            when(collectionService.registerMongCollection(memberId, mongCode))
                    .thenReturn(registerMongCollectionResDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMongCollectionReqDto(mongCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.memberId").value(1L))
                    .andExpect(jsonPath("$.code").value(mongCode))
                    .andExpect(jsonPath("$.createdAt").exists());
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mongCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).registerMongCollection(memberIdCaptor.capture(), mongCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMongCode = mongCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMongCode).isEqualTo(mongCode);
        }

        @Test
        @DisplayName("몽 코드 없이 몽 컬렉션을 등록하면 INVALID_PARAMETER 에러 코드를 반환한다.")
        void registerMongCollectionNotFoundMong() throws Exception {
            // given
            ErrorCode errorCode = CollectionErrorCode.INVALID_PARAMETER;

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMongCollectionReqDto(null))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }

        @Test
        @DisplayName("몽 코드가 아닌 코드로 몽 컬렉션을 등록하면 INVALID_MONG_CODE 에러 코드를 반환한다.")
        void registerMongCollectionInvalidMongCode() throws Exception {
            // given
            Long memberId = 1L;
            String mongCode = "INVALID_CODE";
            ErrorCode errorCode = CollectionErrorCode.INVALID_MONG_CODE;

            when(collectionService.registerMongCollection(memberId, mongCode))
                    .thenThrow(new InvalidCodeException(errorCode));

            // when
            ResultActions resultActions = mockMvc.perform(post("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RegisterMongCollectionReqDto(mongCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }
    }

    @Nested
    @DisplayName("삭제 단위 테스트")
    @WithMockPassportDetail
    class Remove {
        @Test
        @DisplayName("맵 코드로 맵 컬렉션을 삭제한다.")
        void removeMapCollection() throws Exception {
            // given
            Long memberId = 1L;
            String mapCode = testMapCode.code();

            doNothing().when(collectionService).removeMapCollection(memberId, mapCode);

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMapCollectionReqDto(mapCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk());
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mapCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).removeMapCollection(memberIdCaptor.capture(), mapCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMapCode = mapCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMapCode).isEqualTo(mapCode);
        }

        @Test
        @DisplayName("맵 코드 없이 맵 컬렉션을 삭제하면 INVALID_PARAMETER 에러 코드를 반환한다.")
        void removeMapCollectionNotFoundMapCode() throws Exception {
            // given
            ErrorCode errorCode = CollectionErrorCode.INVALID_PARAMETER;

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMapCollectionReqDto(null))));

            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }

        @Test
        @DisplayName("맵 코드가 아닌 코드로 맵 컬렉션을 삭제하면 INVALID_MAP_CODE 에러 코드를 반환한다.")
        void removeMapCollectionInvalidMapCode() throws Exception {
            // given
            Long memberId = 1L;
            String mapCode = "INVALID_CODE";
            ErrorCode errorCode = CollectionErrorCode.INVALID_MAP_CODE;

            doThrow(new InvalidCodeException(errorCode))
                    .when(collectionService).removeMapCollection(memberId, mapCode);

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/map")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMapCollectionReqDto(mapCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));

            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mapCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).removeMapCollection(memberIdCaptor.capture(), mapCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMapCode = mapCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMapCode).isEqualTo(mapCode);
        }

        @Test
        @DisplayName("몽 코드로 몽 컬렉션을 삭제한다.")
        void removeMongCollection() throws Exception {
            // given
            Long memberId = 1L;
            String mongCode = testMongCode.code();

            doNothing().when(collectionService).removeMongCollection(memberId, mongCode);

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMongCollectionReqDto(mongCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().isOk());
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mongCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).removeMongCollection(memberIdCaptor.capture(), mongCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMapCode = mongCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMapCode).isEqualTo(mongCode);
        }

        @Test
        @DisplayName("몽 코드 없이 몽 컬렉션을 삭제하면 INVALID_PARAMETER 에러 코드를 반환한다.")
        void removeMongCollectionNotFoundMongCode() throws Exception {
            // given
            ErrorCode errorCode = CollectionErrorCode.INVALID_PARAMETER;

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMongCollectionReqDto(null))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
        }

        @Test
        @DisplayName("몽 코드가 아닌 코드로 몽 컬렉션을 삭제하면 INVALID_MONG_CODE 에러 코드를 반환한다.")
        void removeMongCollectionInvalidMongCode() throws Exception {
            // given
            Long memberId = 1L;
            String mongCode = "INVALID_CODE";
            ErrorCode errorCode = CollectionErrorCode.INVALID_MONG_CODE;

            doThrow(new InvalidCodeException(errorCode))
                    .when(collectionService).removeMongCollection(memberId, mongCode);

            // when
            ResultActions resultActions = mockMvc.perform(delete("/collection/mong")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new RemoveMongCollectionReqDto(mongCode))));

            // then
            // HttpStatus & contentType
            resultActions
                    .andExpect(status().is(errorCode.getHttpStatus().value()));
            // data
            resultActions
                    .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
            // Parameter
            var memberIdCaptor = ArgumentCaptor.forClass(Long.class);
            var mongCodeCaptor = ArgumentCaptor.forClass(String.class);
            verify(collectionService, times(1)).removeMongCollection(memberIdCaptor.capture(), mongCodeCaptor.capture());

            var passedMemberId = memberIdCaptor.getValue();
            var passedMapCode = mongCodeCaptor.getValue();
            assertThat(passedMemberId).isEqualTo(memberId);
            assertThat(passedMapCode).isEqualTo(mongCode);
        }
    }
}
