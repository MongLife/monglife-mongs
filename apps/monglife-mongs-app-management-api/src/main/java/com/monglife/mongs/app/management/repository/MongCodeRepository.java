package com.monglife.mongs.app.management.repository;

import com.monglife.mongs.app.management.domain.MongCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongCodeRepository extends JpaRepository<MongCodeEntity, String> {
}
