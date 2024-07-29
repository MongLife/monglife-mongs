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

    @Query("SELECT m FROM Mong m WHERE m.isActive = true")
    List<Mong> findAllActive();

    @Query("SELECT m FROM Mong m WHERE m.accountId = :accountId AND m.isActive = true")
    List<Mong> findByAccountIdAndIsActiveTrue(@Param("accountId") Long accountId);

    @Query("SELECT m FROM Mong m WHERE m.id = :mongId AND m.isActive = true")
    Optional<Mong> findByIdAndIsActiveTrue(@Param("mongId") Long mongId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Mong m WHERE m.id = :mongId AND m.isActive = true")
    Optional<Mong> findByIdAndIsActiveTrueWithLock(@Param("mongId") Long mongId);
}
