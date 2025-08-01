package com.monglife.mongs.adapter.out.mong.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.mong.persistence.entity.MongTypeEntity;

import java.util.List;

public interface MongTypeDslRepository {

    List<MongTypeEntity> findMongCode(String mongCode);
}
