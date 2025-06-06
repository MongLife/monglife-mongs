package com.monglife.mongs.adapter.out.device.persistence.repository.dsl;

import com.monglife.mongs.adapter.out.device.persistence.entity.DeviceEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.mongs.adapter.out.device.persistence.entity.QDeviceEntity.deviceEntity;

@Repository
public class DeviceDslRepositoryImpl implements DeviceDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public DeviceDslRepositoryImpl(@Qualifier("deviceJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<DeviceEntity> findByDeviceIdWithLock(String deviceId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(deviceEntity)
                .where(deviceEntity.deviceId.eq(deviceId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
