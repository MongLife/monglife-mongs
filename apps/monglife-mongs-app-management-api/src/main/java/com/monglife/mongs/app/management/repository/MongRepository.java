package com.monglife.mongs.app.management.repository;

import com.monglife.mongs.app.management.domain.MongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongRepository extends JpaRepository<MongEntity, Long> {
}
