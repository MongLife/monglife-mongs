package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.config.GovTemplateProperty;
import com.monglife.mongs.adapter.out.member.persistence.dto.GovStoreResponseDto;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMapEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.CollectionMongEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.MapTypeEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMapRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.CollectionMongRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.MapTypeRepository;
import com.monglife.mongs.application.member.port.out.CollectionReadPort;
import com.monglife.mongs.application.member.port.out.vo.SearchMapVo;
import com.monglife.mongs.domain.member.model.CollectionMap;
import com.monglife.mongs.domain.member.model.CollectionMong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectionReadService implements CollectionReadPort {

    private final CollectionMapRepository collectionMapRepository;

    private final CollectionMongRepository collectionMongRepository;

    private final MapTypeRepository mapTypeRepository;

    private final RestTemplate restTemplate;

    private final GovTemplateProperty govTemplateProperty;

    public CollectionReadService(
            @Autowired CollectionMapRepository collectionMapRepository,
            @Autowired CollectionMongRepository collectionMongRepository,
            @Autowired MapTypeRepository mapTypeRepository,
            @Qualifier("govRestTemplate") RestTemplate restTemplate,
            @Autowired GovTemplateProperty govTemplateProperty
    ) {
        this.collectionMapRepository = collectionMapRepository;
        this.collectionMongRepository = collectionMongRepository;
        this.mapTypeRepository = mapTypeRepository;
        this.restTemplate = restTemplate;
        this.govTemplateProperty = govTemplateProperty;
    }

    /**
     * 컬렉션 맵 존재 여부 조회
     * @param accountId 회원 ID
     * @param mapCode 맵 타입 코드
     * @return 컬렉션 맵 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsCollectionMapPort(Long accountId, String mapCode) {
        return collectionMapRepository.existsByAccountIdAndComnCode(accountId, mapCode);
    }

    /**
     * 컬렉션 몽 존재 여부 조회
     * @param accountId 회원 ID
     * @param mongCode 몽 타입 코드
     * @return 컬렉션 몽 존재 여부
     */
    @Override
    @Transactional
    public Boolean isExistsCollectionMongPort(Long accountId, String mongCode) {
        return collectionMongRepository.existsByAccountIdAndComnCode(accountId, mongCode);
    }

    /**
     * 컬렉션 맵 목록 조회
     * @param accountId 회원 ID
     * @return 컬렉션 맵 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<CollectionMap> getCollectionMapsPort(Long accountId) {

        List<CollectionMapEntity> collectionMapEntities = collectionMapRepository.findByAccountId(accountId);

        return collectionMapEntities.stream()
                .sorted(Comparator.comparing(o -> o.getComn().getCode()))
                .map(CollectionMapEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 컬렉션 몽 목록 조회
     * @param accountId 회원 ID
     * @return 컬렉션 몽 도메인 객체 목록
     */
    @Override
    @Transactional
    public List<CollectionMong> getCollectionMongsPort(Long accountId) {

        List<CollectionMongEntity> collectionMongEntities = collectionMongRepository.findByAccountId(accountId);

        return collectionMongEntities.stream()
                .sorted(Comparator.comparing(o -> o.getComn().getCode()))
                .map(CollectionMongEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 위도 경도 기반 주변 컬렉션 맵 조회
     * @param latitude 위도
     * @param longitude 경도
     * @return 컬렉션 맵 도메인 목록
     */
    @Override
    public Queue<SearchMapVo> searchMapsPort(Double latitude, Double longitude, Integer radius) {

        PriorityQueue<SearchMapVo> searchMapVos = new PriorityQueue<>(Comparator.comparing(SearchMapVo::getDistance));
        List<MapTypeEntity> mapTypeEntities = mapTypeRepository.findAll();

        int page = 1;
        int size = 100;

        String parameter =
                "ServiceKey=" + govTemplateProperty.getServiceKey() + "&" +
                "pageNo=" + page + "&" +
                "numOfRows=" + size + "&" +
                "radius=" + radius + "&" +
                "cx=" + longitude + "&" +
                "cy=" + latitude + "&" +
                "indsLclsCd=" + "I2" + "&" +
                "type=" + "json";

        ResponseEntity<GovStoreResponseDto> responseEntity = restTemplate
            .exchange("?" + parameter, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        if (responseEntity.getBody() != null) {
            for (GovStoreResponseDto.Item item : responseEntity.getBody().getData().getItems()) {
                for (MapTypeEntity mapTypeEntity : mapTypeEntities) {
                    for (String word : mapTypeEntity.getWords().split("\n")) {
                        if (item.getName().contains(word) || item.getSubName().contains(word)) {

                            double dLat = Math.toRadians(latitude - item.getLatitude());
                            double dLon = Math.toRadians(longitude - item.getLongitude());

                            double haversineValue = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                                    + Math.cos(Math.toRadians(item.getLatitude())) * Math.cos(Math.toRadians(latitude))
                                    * Math.sin(dLon / 2) * Math.sin(dLon / 2);

                            double centralAngle = 2 * Math.atan2(Math.sqrt(haversineValue), Math.sqrt(1 - haversineValue));

                            double distance = 6371.0 * centralAngle;

                            searchMapVos.offer(SearchMapVo.builder()
                                            .mapCode(mapTypeEntity.getComn().getCode())
                                            .longitude(item.getLongitude())
                                            .latitude(item.getLatitude())
                                            .distance(distance)
                                            .build());
                        }
                    }
                }
            }
        }

        return searchMapVos;
    }
}
