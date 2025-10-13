package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.module.common.jpa.entity.GroupCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.entity.MapTypeEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.ComnCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.GroupCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.MapTypeRepository;
import com.monglife.mongs.application.member.port.out.MapStoreReadPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AdapterOutMemberPersistenceConfig.class,
        MemberDataSourceConfig.class,
        HibernateAutoConfig.class
})
class MapStoreReadServiceTest {

    private final MapStoreReadPort mapStoreReadPort;
    private final GroupCodeRepository groupCodeRepository;
    private final ComnCodeRepository comnCodeRepository;
    private final MapTypeRepository mapTypeRepository;

    @Autowired
    public MapStoreReadServiceTest(MapStoreReadPort mapStoreReadPort, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository, MapTypeRepository mapTypeRepository) {
        this.mapStoreReadPort = mapStoreReadPort;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
        this.mapTypeRepository = mapTypeRepository;
    }

    // 국자원 화재로 인한 API 접근 불가
    //@Test
    @DisplayName("공공 데이터 API 를 호출하여 반경 내 상가 정보를 조회 한다.")
    void searchMapsPort() {
        // arrange
        GroupCodeEntity groupCodeEntity = GroupCodeEntity.builder()
                .code("MP")
                .name("맵")
                .build();
        ComnCodeEntity comnCodeEntity = ComnCodeEntity.builder()
                .code("MP002")
                .name("이디야")
                .group(groupCodeEntity)
                .build();
        MapTypeEntity mapTypeEntity = MapTypeEntity.builder()
                .mapTypeId(1L)
                .comn(comnCodeEntity)
                .words("이디야")
                .build();

        double latitude = 37.3253212521358;
        double longitude = 127.987835219462;
        int radius = 1;

        groupCodeRepository.saveAndFlush(groupCodeEntity);
        comnCodeRepository.saveAndFlush(comnCodeEntity);
        mapTypeRepository.saveAndFlush(mapTypeEntity);

        // act
        var expected = mapStoreReadPort.searchMapsPort(latitude, longitude, radius);

        // assert
        assertFalse(expected.isEmpty());
    }
}