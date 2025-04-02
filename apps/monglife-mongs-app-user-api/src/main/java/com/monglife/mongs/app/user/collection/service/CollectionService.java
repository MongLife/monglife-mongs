package com.monglife.mongs.app.user.collection.service;

import com.monglife.mongs.domain.member.vo.CollectionMapVo;
import com.monglife.mongs.domain.member.vo.CollectionMongVo;
import com.monglife.mongs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final MemberService memberService;

    @Transactional
    public void createCollectionMap(Long accountId, Double latitude, Double longitude) {

        // TODO: 위도, 경도 기준 근방 300m 내 가게 목록 조회
        Long mapPositionId = 1L;
        String mapTypeCode = "";

        // 맵 컬렉션 등록
//        memberService.createCollectionMap(accountId, mapTypeCode, mapPositionId);
    }

    @Transactional(readOnly = true)
    public List<CollectionMapVo> getCollectionMaps(Long accountId) {
        return memberService.getCollectionMaps(accountId);
    }

    @Transactional
    public void createCollectionMong(Long accountId, String mongTypeCode) {
        memberService.createCollectionMong(accountId, mongTypeCode);
    }

    @Transactional(readOnly = true)
    public List<CollectionMongVo> getCollectionMongs(Long accountId) {
        return memberService.getCollectionMongs(accountId);
    }
}
