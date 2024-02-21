package com.mongs.core.code;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CodeRepositoryImpl implements CodeRepository {

    @Override
    public Optional<List<Code>> findByGroupCode(String groupCode) {
        return switch (groupCode) {
            case "MP" -> Optional.of(
                    Arrays.stream(MapCode.values())
                            .map(mapCode -> (Code) mapCode)
                            .toList());
            case "AT" -> Optional.of(
                    Arrays.stream(MongActiveCode.values())
                            .map(mongActiveCode -> (Code) mongActiveCode)
                            .toList());
            case "CH" -> Optional.of(
                    Arrays.stream(MongCode.values())
                            .map(mongCode -> (Code) mongCode)
                            .toList());
            default -> Optional.of(
                    Arrays.stream(MongConditionCode.values())
                            .map(mongConditionCode -> (Code) mongConditionCode)
                            .toList());
        };
    }

    @Override
    public Optional<Code> findByCode(String code) {
        String groupCode = code.substring(0, 2);

        return switch (groupCode) {
            case "MP" -> Optional.of(Arrays.stream(MapCode.values())
                    .filter(mapCode -> mapCode.getCode().equals(code)).findFirst().get());
            case "AT" -> Optional.of(Arrays.stream(MongActiveCode.values())
                    .filter(mongActiveCode -> mongActiveCode.getCode().equals(code)).findFirst().get());
            case "CH" -> Optional.of(Arrays.stream(MongCode.values())
                    .filter(mongCode -> mongCode.getCode().equals(code)).findFirst().get());
            default -> Optional.of(Arrays.stream(MongConditionCode.values())
                    .filter(mongConditionCode -> mongConditionCode.getCode().equals(code)).findFirst().get());
        };
    }
}
