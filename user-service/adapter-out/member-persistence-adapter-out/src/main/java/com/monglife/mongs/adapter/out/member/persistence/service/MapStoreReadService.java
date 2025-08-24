package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.mongs.adapter.out.member.persistence.config.GovTemplateProperty;
import com.monglife.mongs.adapter.out.member.persistence.dto.GovStoreResponseDto;
import com.monglife.mongs.adapter.out.member.persistence.entity.MapTypeEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.MapTypeRepository;
import com.monglife.mongs.application.member.port.out.MapStoreReadPort;
import com.monglife.mongs.application.member.port.out.vo.SearchMapVo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class MapStoreReadService implements MapStoreReadPort {

    private final MapTypeRepository mapTypeRepository;

    private final RestTemplate restTemplate;

    private final GovTemplateProperty govTemplateProperty;

    /**
     * 위도 경도 기반 주변 컬렉션 맵 조회
     * @param latitude 위도
     * @param longitude 경도
     * @return 컬렉션 맵 도메인 목록
     */
    @Override
    public Queue<SearchMapVo> searchMapsPort(Double latitude, Double longitude, Integer radius) {

        List<MapTypeEntity> mapTypeEntities = mapTypeRepository.findAll();

        int page = 1;
        int size = 100;

        URI uri = UriComponentsBuilder.fromUri(URI.create(govTemplateProperty.getUrl()))
                .path(govTemplateProperty.getPath())
                .queryParam("ServiceKey", URLEncoder.encode(govTemplateProperty.getServiceKey(), StandardCharsets.UTF_8))
                .queryParam("pageNo", page)
                .queryParam("numOfRows", size)
                .queryParam("radius", radius)
                .queryParam("cx", longitude)
                .queryParam("cy", latitude)
                .queryParam("indsLclsCd", "I2")
                .queryParam("type", "json")
                .build(true)
                .toUri();

        ResponseEntity<GovStoreResponseDto> responseEntity = restTemplate
                .exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        PriorityQueue<SearchMapVo> searchMapVos = new PriorityQueue<>(Comparator.comparing(SearchMapVo::getDistance));

        if (responseEntity.getBody() != null && responseEntity.getBody().getData().getItems() != null) {
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
