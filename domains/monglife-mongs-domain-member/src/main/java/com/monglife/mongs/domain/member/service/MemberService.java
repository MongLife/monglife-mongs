package com.monglife.mongs.domain.member.service;

import com.monglife.mongs.domain.member.dto.etc.GetCollectionMapDto;
import com.monglife.mongs.domain.member.dto.etc.GetCollectionMongDto;
import com.monglife.mongs.domain.member.dto.etc.GetMemberDto;
import com.monglife.mongs.domain.member.entity.*;
import com.monglife.mongs.domain.member.exception.*;
import com.monglife.mongs.domain.member.repository.ComnCodeRepository;
import com.monglife.mongs.domain.member.repository.MapPositionRepository;
import com.monglife.mongs.domain.member.repository.MemberRepository;
import com.monglife.mongs.module.jpa.entity.ComnCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ComnCodeRepository comnCodeRepository;
    private final MapPositionRepository mapPositionRepository;

    /**
     * 회원 생성
     * @param accountId 계정 ID
     */
    @Transactional
    public void createMember(Long accountId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElse(new MemberEntity(accountId));

        memberRepository.save(memberEntity);
    }

    /**
     * 회원 조회
     * @param accountId 계정 ID
     * @return 회원 정보
     */
    @Transactional(readOnly = true)
    public GetMemberDto getMember(Long accountId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        return GetMemberDto.builder()
                .accountId(memberEntity.getAccountId())
                .slotCount(memberEntity.getSlotCount())
                .starPoint(memberEntity.getStarPoint())
                .build();
    }

    /**
     * 컬렉션 맵 등록
     * @param accountId 계정 ID
     * @param mapTypeCode 맵 타입 코드
     */
    @Transactional
    public void createCollectionMap(Long accountId, String mapTypeCode, Long mapPositionId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(mapTypeCode)
                .orElseThrow(() -> new NotExistsMapTypeCodeException(mapTypeCode));

        MapPositionEntity mapPositionEntity = mapPositionRepository.findById(mapPositionId)
                .orElseThrow(() -> new NotExistsMapPositionException(mapPositionId));

        CollectionMapEntity collectionMapEntity = CollectionMapEntity.builder()
                .accountId(accountId)
                .comn(comnCodeEntity)
                .position(mapPositionEntity)
                .build();

        memberEntity.joinCollectionMap(collectionMapEntity);
    }

    /**
     * 컬렉션 맵 조회
     * @param accountId 계정 ID
     * @return 컬렉션 맵 목록
     */
    @Transactional(readOnly = true)
    public List<GetCollectionMapDto> getCollectionMaps(Long accountId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        List<CollectionMapEntity> collectionMapEntities = memberEntity.getCollectionMaps();

        return collectionMapEntities.stream()
                .map(GetCollectionMapDto::of)
                .toList();
    }

    /**
     * 컬렉션 몽 등록
     * @param accountId 계정 ID
     * @param mongTypeCode 몽 타입 코드
     */
    @Transactional
    public void createCollectionMong(Long accountId, String mongTypeCode) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        ComnCodeEntity comnCodeEntity = comnCodeRepository.findById(mongTypeCode)
                .orElseThrow(() -> new NotExistsMongTypeCodeException(mongTypeCode));

        CollectionMongEntity collectionMongEntity = CollectionMongEntity.builder()
                .accountId(accountId)
                .comn(comnCodeEntity)
                .build();

        memberEntity.joinCollectionMong(collectionMongEntity);
    }

    /**
     * 컬렉션 몽 조회
     * @param accountId 계정 ID
     * @return 컬렉션 몽 목록
     */
    @Transactional(readOnly = true)
    public List<GetCollectionMongDto> getCollectionMongs(Long accountId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        List<CollectionMongEntity> collectionMongEntities = memberEntity.getCollectionMongs();

        return collectionMongEntities.stream()
                .map(GetCollectionMongDto::of)
                .toList();
    }

    /**
     * 피드백 등록
     * @param accountId 계정 ID
     * @param deviceId 디바이스 ID
     * @param deviceName 디바이스명
     * @param title 피드백 제목
     * @param content 피드백 본문
     */
    @Transactional
    public void createFeedback(Long accountId, String deviceId, String deviceName, String title, String content) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        FeedbackEntity feedbackEntity = FeedbackEntity.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .deviceName(deviceName)
                .title(title)
                .content(content)
                .build();

        memberEntity.joinFeedback(feedbackEntity);
    }


    @Transactional
    public void increaseSlot(Long accountId) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        memberEntity.increaseSlotCount();
    }

    @Transactional
    public void increaseStarPoint(Long accountId, Integer starPoint) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        memberEntity.increaseStarPoint(starPoint);
    }

    @Transactional
    public void decreaseStarPoint(Long accountId, Integer starPoint) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        if (memberEntity.getStarPoint() < starPoint) throw new NotExistsStarPointException(starPoint);

        memberEntity.decreaseStarPoint(starPoint);
    }

    @Transactional
    public void increaseWalkingCount(Long accountId, Integer walkingCount) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        memberEntity.increaseWalkingCount(walkingCount);
    }

    @Transactional
    public void decreaseWalkingCount(Long accountId, Integer walkingCount) {

        MemberEntity memberEntity = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotExistsMemberException(accountId));

        memberEntity.decreaseWalkingCount(walkingCount);
    }
}
