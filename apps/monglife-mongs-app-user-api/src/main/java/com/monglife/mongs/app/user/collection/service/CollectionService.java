package com.monglife.mongs.app.user.collection.service;

import com.monglife.mongs.domain.member.dto.etc.GetCollectionMapDto;
import com.monglife.mongs.domain.member.dto.etc.GetCollectionMongDto;
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
    public void createCollectionMap(Long accountId, String mapTypeCode) {

        // TODO: 맵 주소 엔티티
        Long mapPositionId = 1L;

        memberService.createCollectionMap(accountId, mapTypeCode, mapPositionId);
    }

    @Transactional(readOnly = true)
    public List<GetCollectionMapDto> getCollectionMaps(Long accountId) {
        return memberService.getCollectionMaps(accountId);
    }

    @Transactional
    public void createCollectionMong(Long accountId, String mongTypeCode) {
        memberService.createCollectionMong(accountId, mongTypeCode);
    }

    @Transactional(readOnly = true)
    public List<GetCollectionMongDto> getCollectionMongs(Long accountId) {
        return memberService.getCollectionMongs(accountId);
    }
}
