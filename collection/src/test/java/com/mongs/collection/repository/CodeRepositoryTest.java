package com.mongs.collection.repository;

import com.mongs.core.code.Code;
import com.mongs.core.code.CodeRepository;
import com.mongs.core.code.GroupCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CodeRepositoryTest {
    @Autowired
    private CodeRepository codeRepository;

    @Test
    @DisplayName("그룹코드를 기준으로 조회 했을 때, 값이 없는 경우 빈 리스트를 반환한다.")
    void findAllByGroupCodeEmpty() {
        // given
        String groupCode = "INVALID_GROUP_CODE";

        // when
        List<Code> expected = codeRepository.findByGroupCode(groupCode);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected).hasSize(0);
    }

    @Test
    @DisplayName("코드를 기준으로 조회했을 때, 값이 없는 경우 Optional.empty() 를 반환한다.")
    void findConditionByCodeEmpty() {
        // given
        String code = "INVALID_CODE";

        // when
        Optional<Code> expected = codeRepository.findByCode(code);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    @DisplayName("맵 그룹코드를 기준으로 조회하면 맵 Code 리스트를 반환한다.")
    void findMapAllByGroupCode() {
        // given
        String groupCode = GroupCode.MAP.getGroupCode();

        // when
        List<Code> mapCodeList = codeRepository.findByGroupCode(groupCode);

        // then
        assertThat(mapCodeList).isNotEmpty();
        mapCodeList.forEach(code -> {
            assertThat(code.getCode().contains(groupCode)).isTrue();
        });
    }

    @Test
    @DisplayName("몽 그룹코드를 기준으로 조회하면 몽 Code 리스트를 반환한다.")
    void findMongAllByGroupCode() {
        // given
        String groupCode = GroupCode.MONG.getGroupCode();

        // when
        List<Code> mongCodeList = codeRepository.findByGroupCode(groupCode);

        // then
        assertThat(mongCodeList).isNotEmpty();
        mongCodeList.forEach(code -> {
            assertThat(code.getCode().contains(groupCode)).isTrue();
        });
    }

    @Test
    @DisplayName("활동 그룹코드를 기준으로 조회하면 활동 Code 리스트를 반환한다.")
    void findActiveAllByGroupCode() {
        // given
        String groupCode = GroupCode.ACTIVE.getGroupCode();

        // when
        List<Code> activeCodeList = codeRepository.findByGroupCode(groupCode);

        // then
        assertThat(activeCodeList).isNotEmpty();
        activeCodeList.forEach(code -> {
            assertThat(code.getCode().contains(groupCode)).isTrue();
        });
    }

    @Test
    @DisplayName("상태 그룹코드를 기준으로 조회하면 상태 Code 리스트를 반환한다.")
    void findConditionAllByGroupCode() {
        // given
        String groupCode = GroupCode.CONDITION.getGroupCode();

        // when
        List<Code> conditionCodeList = codeRepository.findByGroupCode(groupCode);

        // then
        assertThat(conditionCodeList).isNotEmpty();
        conditionCodeList.forEach(code -> {
            assertThat(code.getCode().contains(groupCode)).isTrue();
        });
    }

    @Test
    @DisplayName("맵 코드를 기준으로 조회하면 맵 Code 를 반환한다.")
    void findMapByCode() {
        // given
        String code = "MP000";
        String name = "기본";

        // when
        Optional<Code> mapCode = codeRepository.findByCode(code);

        // then
        assertThat(mapCode.isPresent()).isTrue();
        assertThat(mapCode.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("몽 코드를 기준으로 조회하면 몽 Code 를 반환한다.")
    void findMongByCode() {
        // given
        String code = "CH000";
        String name = "화산알";

        // when
        Optional<Code> mongCode = codeRepository.findByCode(code);

        // then
        assertThat(mongCode.isPresent()).isTrue();
        assertThat(mongCode.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("활동 코드를 기준으로 조회하면 활동 Code 를 반환한다.")
    void findActiveByCode() {
        // given
        String code = "AT000";
        String name = "식사";

        // when
        Optional<Code> activeCode = codeRepository.findByCode(code);

        // then
        assertThat(activeCode.isPresent()).isTrue();
        assertThat(activeCode.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상태 코드를 기준으로 조회하면 상태 Code 를 반환한다.")
    void findConditionByCode() {
        // given
        String code = "CD000";
        String name = "정상";

        // when
        Optional<Code> conditionCode = codeRepository.findByCode(code);

        // then
        assertThat(conditionCode.isPresent()).isTrue();
        assertThat(conditionCode.get().getName()).isEqualTo(name);
    }
}
