package com.mongs.play.domain.account.repository;

import com.mongs.play.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.isDeleted = false")
    Optional<Account> findByEmailAndIsDeletedFalse(@Param("email") String email);

    @Query("SELECT a FROM Account a WHERE a.id = :accountId AND a.isDeleted = false")
    Optional<Account> findByIdAndIsDeletedFalse(@Param("accountId") Long accountId);
}
