package com.monglife.mongs.domain.mong.repository;

import com.monglife.mongs.domain.mong.entity.MongTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongTypeRepository extends JpaRepository<MongTypeEntity, String> {

    List<MongTypeEntity> findByGroupType(String groupType);
}
