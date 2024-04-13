package com.mongs.common.service;

import com.mongs.common.code.InitFeedbackCodeData;
import com.mongs.common.code.InitFoodCodeData;
import com.mongs.common.code.InitMapCodeData;
import com.mongs.common.code.InitMongCodeData;
import com.mongs.common.controller.dto.response.*;
import com.mongs.common.entity.CodeVersion;
import com.mongs.common.exception.CommonErrorCode;
import com.mongs.common.exception.NotFoundVersionException;
import com.mongs.common.repository.*;
import com.mongs.common.service.vo.*;
import com.mongs.core.entity.FeedbackCode;
import com.mongs.core.entity.FoodCode;
import com.mongs.core.entity.MapCode;
import com.mongs.core.entity.MongCode;
import com.mongs.core.utils.HmacProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final HmacProvider hmacProvider;
    private final CodeVersionRepository codeVersionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    /**
     * 코드 버전 값을 확인한다.
     * 앱 빌드 버전에 해당하는 CodeVersion 을 확인하고, 해당 버전의 앱 업데이트가 필요한지 여부를 확인
     * 코드 해싱 값을 확인하여 앱 빌드 버전에 해당하는 해싱 값과 비교하여 코드 업데이트가 필요한지 여부를 확인
     *
     * @param buildVersion 앱 빌드 버전
     * @param codeIntegrity 앱 내 코드 리스트 해싱 값
     * @return {@link FindVersionVo}
     * **/
    public FindVersionVo findVersion(String buildVersion, String codeIntegrity) {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                .orElseThrow(() -> new NotFoundVersionException(CommonErrorCode.NOT_FOUND_VERSION));

        return FindVersionVo.builder()
                .newestBuildVersion(codeVersion.buildVersion())
                .createdAt(codeVersion.createdAt())
                /* 리소스가 추가되어 이전 버전들에 대한 앱 업데이트 여부 확인 */
                .mustUpdateApp(codeVersion.mustUpdateApp())
                /* 해싱 값이 다르면 코드 업데이트 */
                .mustUpdateCode(!codeVersion.codeIntegrity().equals(codeIntegrity))
                .build();
    }

    /**
     * 앱 빌드 버전에 해당하는 맵 코드 리스트를 조회한다.
     * 앱 빌드 버전보다 아래에 있는 맵 코드 값들을 조회하여 반환한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return {@link List<FindMapCodeResDto>}
     */
    public List<FindMapCodeResDto> findMapCode(String buildVersion) {
        return FindMapCodeResDto.toList(mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion));
    }

    /**
     * 앱 빌드 버전에 해당하는 몽 코드 리스트를 조회한다.
     * 앱 빌드 버전보다 아래에 있는 몽 코드 값들을 조회하여 반환한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return {@link List<FindMongCodeResDto>}
     */
    public List<FindMongCodeResDto> findMongCode(String buildVersion) {
        return FindMongCodeResDto.toList(mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion));
    }

    /**
     * 앱 빌드 버전에 해당하는 음식 코드 리스트를 조회한다.
     * 앱 빌드 버전보다 아래에 있는 음식 코드 값들을 조회하여 반환한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return {@link List<FindFoodCodeResDto>}
     */
    public List<FindFoodCodeResDto> findFoodCode(String buildVersion) {
        return FindFoodCodeResDto.toList(foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion));
    }

    /**
     * 앱 빌드 버전에 해당하는 피드백 코드 리스트를 조회한다.
     * 앱 빌드 버전보다 아래에 있는 피드백 코드 값들을 조회하여 반환한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return {@link List<FindFeedbackCodeResDto>}
     */
    public List<FindFeedbackCodeResDto> findFeedbackCode(String buildVersion) {
        return FindFeedbackCodeResDto.toList(feedbackCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion));
    }

    /**
     * 앱 빌드 버전에 해당하는 코드 해싱 값을 생성 한다.
     *
     * @param buildVersion 앱 빌드 버전
     * @return 코드 해싱 값
     */
    public String generateIntegrity(String buildVersion) {
        return hmacProvider.generateHmac(FindCodeResDto.builder()
                        .mapCodeList(this.findMapCode(buildVersion))
                        .mongCodeList(this.findMongCode(buildVersion))
                        .foodCodeList(this.findFoodCode(buildVersion))
                        .feedbackCodeList(this.findFeedbackCode(buildVersion))
                        .build())
                .orElseThrow(RuntimeException::new);
    }

    /**
     * 코드의 값을 buildVersion 1.0.0 으로 초기화 시킨다.
     * 코드 동일성 확인을 위한 해싱 값도 초기화 시킨다.
     *
     */
    public void initializeCode() {
        String buildVersion = "1.0.0";

        foodCodeRepository.deleteAll();
        Arrays.stream(InitFoodCodeData.values()).forEach(foodCode -> {
            foodCodeRepository.save(FoodCode.builder()
                    .buildVersion(buildVersion)
                    .code(foodCode.getCode())
                    .name(foodCode.getName())
                    .groupCode(foodCode.getGroupCode())
                    .addHealthyValue(foodCode.getAddHealthyValue())
                    .addSleepValue(foodCode.getAddSleepValue())
                    .addSatietyValue(foodCode.getAddSatietyValue())
                    .addWeightValue(foodCode.getAddWeightValue())
                    .addStrengthValue(foodCode.getAddStrengthValue())
                    .price(foodCode.getPrice())
                    .delaySeconds(foodCode.getDelaySeconds())
                    .build());
        });

        mapCodeRepository.deleteAll();
        Arrays.stream(InitMapCodeData.values()).forEach(mapCode -> {
            mapCodeRepository.save(MapCode.builder()
                    .buildVersion(buildVersion)
                    .code(mapCode.getCode())
                    .name(mapCode.getName())
                    .build());
        });

        mongCodeRepository.deleteAll();
        Arrays.stream(InitMongCodeData.values()).forEach(mongCode -> {
            mongCodeRepository.save(MongCode.builder()
                    .buildVersion(buildVersion)
                    .code(mongCode.getCode())
                    .name(mongCode.getName())
                    .build());
        });

        feedbackCodeRepository.deleteAll();
        Arrays.stream(InitFeedbackCodeData.values()).forEach(feedbackCode -> {
            feedbackCodeRepository.save(FeedbackCode.builder()
                    .buildVersion(buildVersion)
                    .code(feedbackCode.getCode())
                    .groupCode(feedbackCode.getGroupCode())
                    .message(feedbackCode.getMessage())
                    .build());
        });

        String codeIntegrity = this.generateIntegrity(buildVersion);

        codeVersionRepository.deleteAll();
        codeVersionRepository.save(CodeVersion.builder()
                .buildVersion(buildVersion)
                .codeIntegrity(codeIntegrity)
                .mustUpdateApp(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    /**
     * 리소스가 추가되는 코드인 맵, 몽, 음식 코드를 추가하는 경우
     * 해당 이전 앱 빌드 버전에 대한 앱 업데이트가 필요하기 때문에,
     * 앱 업데이트 필요하다는 플래그를 true 로 변경한다.
     *
     * @param buildVersion 앱 빌드 버전
     */
    private void modifyMustUpdateApp(String buildVersion) {
        CodeVersion newestCodeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                        .orElseGet(() -> {
                            String codeIntegrity = this.generateIntegrity(buildVersion);
                            return codeVersionRepository.save(CodeVersion.builder()
                                    .buildVersion(buildVersion)
                                    .codeIntegrity(codeIntegrity)
                                    .mustUpdateApp(false)
                                    .createdAt(LocalDateTime.now())
                                    .build());
                        });

        /* 이전 버전들에 대해 업데이트 여부 true 로 변경 */
        codeVersionRepository.findByBuildVersionIsBefore(newestCodeVersion.buildVersion()).forEach(codeVersion -> {
            codeVersionRepository.save(codeVersion.toBuilder()
                    .mustUpdateApp(true)
                    .build());
        });
    }

    private void modifyIntegrity(String buildVersion) {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                        .orElseGet(() -> CodeVersion.builder()
                                    .buildVersion(buildVersion)
                                    .mustUpdateApp(false)
                                    .createdAt(LocalDateTime.now())
                                    .build());

        String codeIntegrity = this.generateIntegrity(buildVersion);

        codeVersionRepository.save(codeVersion.toBuilder()
                .codeIntegrity(codeIntegrity)
                .build());
    }

    /**
     * 음식 코드 정보를 등록한다.
     *
     * @param registerFoodCodeVo 음식 코드 정보
     * @param buildVersion 앱 빌드 버전
     */
    public void registerFoodCode(RegisterFoodCodeVo registerFoodCodeVo, String buildVersion) {
        foodCodeRepository.save(FoodCode.builder()
                .code(registerFoodCodeVo.code())
                .name(registerFoodCodeVo.name())
                .groupCode(registerFoodCodeVo.groupCode())
                .price(registerFoodCodeVo.price())
                .addWeightValue(registerFoodCodeVo.addWeightValue())
                .addStrengthValue(registerFoodCodeVo.addStrengthValue())
                .addSatietyValue(registerFoodCodeVo.addSatietyValue())
                .addHealthyValue(registerFoodCodeVo.addHealthyValue())
                .addSleepValue(registerFoodCodeVo.addSleepValue())
                .delaySeconds(registerFoodCodeVo.delaySeconds())
                .buildVersion(buildVersion)
                .build());
        this.modifyMustUpdateApp(buildVersion);
    }

    /**
     * 맵 코드 정보를 등록한다.
     *
     * @param registerMapCodeVo 맵 코드 정보
     * @param buildVersion 앱 빌드 버전
     */
    public void registerMapCode(RegisterMapCodeVo registerMapCodeVo, String buildVersion) {
        mapCodeRepository.save(MapCode.builder()
                .code(registerMapCodeVo.code())
                .name(registerMapCodeVo.name())
                .buildVersion(buildVersion)
                .build());
        this.modifyMustUpdateApp(buildVersion);
    }

    /**
     * 몽 코드 정보를 등록한다.
     *
     * @param registerMongCodeVo 몽 코드 정보
     * @param buildVersion 앱 빌드 버전
     */
    public void registerMongCode(RegisterMongCodeVo registerMongCodeVo, String buildVersion) {
        mongCodeRepository.save(MongCode.builder()
                .code(registerMongCodeVo.code())
                .name(registerMongCodeVo.name())
                .buildVersion(buildVersion)
                .build());
        this.modifyMustUpdateApp(buildVersion);
    }

    /**
     * 피드백 코드 정보를 등록한다.
     *
     * @param registerFeedbackCodeVo 피드백 코드 정보
     * @param buildVersion 앱 빌드 버전
     */
    public void registerFeedbackCode(RegisterFeedbackCodeVo registerFeedbackCodeVo, String buildVersion) {
        feedbackCodeRepository.save(FeedbackCode.builder()
                .code(registerFeedbackCodeVo.code())
                .groupCode(registerFeedbackCodeVo.groupCode())
                .message(registerFeedbackCodeVo.message())
                .buildVersion(buildVersion)
                .build());
        this.modifyIntegrity(buildVersion);
    }
}
