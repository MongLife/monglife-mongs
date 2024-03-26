package com.mongs.auth.repository;

import com.mongs.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {
    Optional<Token>  findTokenByDeviceIdAndAccountId(String deviceId, Long accountId);
}
