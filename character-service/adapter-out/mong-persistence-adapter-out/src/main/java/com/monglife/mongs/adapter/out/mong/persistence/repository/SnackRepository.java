package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.SnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnackRepository extends JpaRepository<SnackEntity, Long> {

    Optional<SnackEntity> findByComnCode(String snackCode);
}
