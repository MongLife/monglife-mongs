package com.mongs.core.code;

import java.util.List;
import java.util.Optional;

public interface CodeRepository {
    List<Code> findByGroupCode(String groupCode);
    Optional<Code> findByCode(String code);
    Optional<Code> findByGroupCodeAndCode(String groupCode, String code);
}
