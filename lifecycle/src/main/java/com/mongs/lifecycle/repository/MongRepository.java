package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MongRepository extends JpaRepository<Mong, Long> {
    Optional<Mong> findByIdAndIsActiveTrue(Long id);
    Optional<Mong> findByIdAndAccountIdAndIsActiveTrue(Long id, Long accountId);
}
