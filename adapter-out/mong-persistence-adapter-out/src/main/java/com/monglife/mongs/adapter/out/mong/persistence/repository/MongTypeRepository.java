package com.monglife.mongs.adapter.out.mong.persistence.repository;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MongTypeRepository extends JpaRepository<MongTypeEntity, String> {

    List<MongTypeEntity> findByGroupType(String groupType);
}
