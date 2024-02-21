package com.mongs.core.code;

import java.util.List;
import java.util.Optional;

public interface CodeRepository {
    Optional<List<Code>> findByGroupCode(String groupCode);
    Optional<Code> findByCode(String code);
}
