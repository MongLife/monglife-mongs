package com.mongs.auth.repository;

import com.mongs.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
