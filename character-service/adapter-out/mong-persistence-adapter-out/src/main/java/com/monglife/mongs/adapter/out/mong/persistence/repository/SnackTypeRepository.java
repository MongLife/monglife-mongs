package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.SnackTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnackTypeRepository extends JpaRepository<SnackTypeEntity, Long> {

    Optional<SnackTypeEntity> findByComnCode(String snackCode);
}
