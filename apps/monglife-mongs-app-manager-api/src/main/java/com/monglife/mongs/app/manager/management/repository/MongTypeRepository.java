package com.monglife.mongs.app.manager.management.repository;

import com.monglife.mongs.app.manager.management.domain.MongTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongTypeRepository extends JpaRepository<MongTypeEntity, String> {

    List<MongTypeEntity> findByMongCodeGroupCode(String groupCode);
}
