package com.monglife.mongs.app.management.repository;

import com.monglife.mongs.app.management.domain.MapCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapCodeRepository extends JpaRepository<MapCodeEntity, String> {
}
