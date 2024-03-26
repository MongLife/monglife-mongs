package com.mongs.auth.repository;

import com.mongs.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailAndIsDeletedFalse(String email);
    Optional<Account> findByIdAndIsDeletedFalse(Long accountId);
}
