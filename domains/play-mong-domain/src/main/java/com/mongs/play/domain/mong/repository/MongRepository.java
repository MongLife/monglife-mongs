package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.Mong;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<Mong, Long> {

    List<Mong> findByAccountIdAndIsActiveTrue(Long accountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Mong> findByIdAndIsActiveTrue(Long mongId);
}
