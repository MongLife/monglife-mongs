package com.monglife.mongs.client.manager.service;

import com.monglife.mongs.client.manager.client.ManagementClient;
import com.monglife.mongs.client.manager.dto.request.ChargePayPointRequestDto;
import com.monglife.mongs.client.manager.dto.request.PatchMongAfterTrainingRequestDto;
import com.monglife.mongs.client.manager.exception.ChargePayPointException;
import com.monglife.mongs.client.manager.exception.GetMinimalMongException;
import com.monglife.mongs.client.manager.exception.PatchMongAfterTrainingException;
import com.monglife.mongs.client.manager.vo.MongVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementClient managementClient;

    /**
     * 페이 포인트 충전
     * @param mongId 몽 ID
     * @param payPoint 증가할 페이 포인트
     */
    public void chargePayPoint(Long mongId, Integer payPoint) {

        try {
            ChargePayPointRequestDto chargePayPointRequestDto = ChargePayPointRequestDto.builder()
                    .payPoint(payPoint)
                    .build();

            managementClient.chargePayPoint(mongId, chargePayPointRequestDto);

        } catch (Exception e) {
            throw new ChargePayPointException(mongId);
        }
    }

    /**
     * 훈련 이후 몽 정보 갱신
     * @param mongId 몽 ID
     * @param exp 경험치
     * @param weight 몸무게
     * @param strength 힘
     * @param satiety 포만감
     * @param healthy 체력
     * @param fatigue 피로도
     * @param poopCount 배변 수
     * @param payPoint 페이 포인트
     */
    public void patchMongAfterTraining(Long mongId, Double exp, Double weight, Double strength, Double satiety, Double healthy, Double fatigue, Integer poopCount, Integer payPoint) {

        try {
            PatchMongAfterTrainingRequestDto patchMongAfterTrainingRequestDto = PatchMongAfterTrainingRequestDto.builder()
                    .exp(exp)
                    .weight(weight)
                    .strength(strength)
                    .satiety(satiety)
                    .healthy(healthy)
                    .fatigue(fatigue)
                    .poopCount(poopCount)
                    .payPoint(payPoint)
                    .build();

            managementClient.patchMongAfterTraining(mongId, patchMongAfterTrainingRequestDto);

        } catch (Exception e) {
            throw new PatchMongAfterTrainingException(mongId);
        }
    }

    /**
     * 몽 정보 조회
     * @param mongId 몽 ID
     * @return 몽 정보 Vo
     */
    public Optional<MongVo> getMong(Long mongId) {

        MongVo mongVo = null;

        try {
            var response = managementClient.getMong(mongId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                mongVo = MongVo.of(response.getBody().getResult());
            }

        } catch (Exception e) {
            throw new GetMinimalMongException(mongId);
        }

        return Optional.ofNullable(mongVo);
    }
}
