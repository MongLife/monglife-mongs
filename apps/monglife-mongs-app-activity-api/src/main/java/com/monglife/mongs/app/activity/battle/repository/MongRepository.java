package com.monglife.mongs.app.activity.battle.repository;

import com.monglife.mongs.app.activity.battle.domain.MongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MongRepository extends JpaRepository<MongEntity, Long> {
}
