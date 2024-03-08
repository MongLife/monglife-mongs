package com.mongs.lifecycle.repository;

import com.mongs.lifecycle.entity.Mong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MongRepository extends JpaRepository<Mong, Long> {
    Optional<Mong> findAllByIdAndIsActiveFalse(Long id);
}
