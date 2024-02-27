package com.mongs.core.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CodeRepositoryImpl implements CodeRepository {

    @Override
    public List<Code> findByGroupCode(String groupCode) {
        if (groupCode.equals(GroupCode.MAP.getGroupCode())) {
            return Arrays.stream(MapCode.values())
                            .map(mapCode -> (Code) mapCode)
                            .toList();
        } else if (groupCode.equals(GroupCode.ACTIVE.getGroupCode())) {
            return Arrays.stream(MongActiveCode.values())
                            .map(mongActiveCode -> (Code) mongActiveCode)
                            .toList();

        } else if (groupCode.equals(GroupCode.MONG.getGroupCode())) {
            return Arrays.stream(MongCode.values())
                            .map(mongCode -> (Code) mongCode)
                            .toList();
        } else if (groupCode.equals(GroupCode.CONDITION.getGroupCode())) {
            return Arrays.stream(MongConditionCode.values())
                            .map(mongConditionCode -> (Code) mongConditionCode)
                            .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Code> findByCode(String code) {
        String groupCode = code.substring(0, 2);
        return findByGroupCodeAndCode(groupCode, code);
    }

    @Override
    public Optional<Code> findByGroupCodeAndCode(String groupCode, String code) {
        if (groupCode.equals(GroupCode.MAP.getGroupCode())) {
            return Arrays.stream(MapCode.values())
                    .filter(mapCode -> mapCode.getCode().equals(code))
                    .map(mapCode -> (Code) mapCode)
                    .findFirst();
        } else if (groupCode.equals(GroupCode.ACTIVE.getGroupCode())) {
            return Arrays.stream(MongActiveCode.values())
                    .filter(mongActiveCode -> mongActiveCode.getCode().equals(code))
                    .map(mongActiveCode -> (Code) mongActiveCode)
                    .findFirst();
        } else if (groupCode.equals(GroupCode.MONG.getGroupCode())) {
            return Arrays.stream(MongCode.values())
                    .filter(mongCode -> mongCode.getCode().equals(code))
                    .map(mongCode -> (Code) mongCode)
                    .findFirst();
        } else if (groupCode.equals(GroupCode.CONDITION.getGroupCode())) {
            return Arrays.stream(MongConditionCode.values())
                    .filter(mongConditionCode -> mongConditionCode.getCode().equals(code))
                    .map(mongConditionCode -> (Code) mongConditionCode)
                    .findFirst();
        } else {
            return Optional.empty();
        }    }
}
