package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<Mong, Long> {
    Optional<Mong> findByIdAndIsActiveTrue(Long mongId);
    Optional<Mong> findByIdAndAccountIdAndIsActiveTrue(Long id, Long accountId);
    List<Mong> findByAccountIdAndIsActiveTrue(Long accountId);
}
