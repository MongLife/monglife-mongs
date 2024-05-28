package com.mongs.play.domain.mong.repository;

import com.mongs.play.domain.mong.entity.Mong;
import jakarta.persistence.LockModeType;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongRepository extends JpaRepository<Mong, Long> {
    List<Mong> findByAccountIdAndIsActiveTrue(Long accountId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Mong> findByIdAndIsActiveTrue(Long mongId);
}
