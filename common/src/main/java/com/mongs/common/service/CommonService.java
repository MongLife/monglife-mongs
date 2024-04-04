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
                .mustUpdateApp(codeVersion.mustUpdateApp())                             /* 리소스가 추가되어 이전 버전들에 대한 앱 업데이트 여부 확인 */
                .mustUpdateCode(!codeVersion.codeIntegrity().equals(codeIntegrity))     /* 해싱 값이 다르면 코드 업데이트 */
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

//    private void modifyMustUpdateApp(String buildVersion) {
//        CodeVersion newestCodeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
//                        .orElseGet(() -> {
//                            String codeIntegrity = hmacProvider.generateHmac(FindCodeResDto.builder()
//                                            .mapCodeList(this.findMapCode(buildVersion))
//                                            .mongCodeList(this.findMongCode(buildVersion))
//                                            .foodCodeList(this.findFoodCode(buildVersion))
//                                            .feedbackCodeList(this.findFeedbackCode(buildVersion))
//                                            .build())
//                                    .orElseThrow(RuntimeException::new);
//
//                            return codeVersionRepository.save(CodeVersion.builder()
//                                    .buildVersion(buildVersion)
//                                    .codeIntegrity(codeIntegrity)
//                                    .mustUpdateApp(false)
//                                    .createdAt(LocalDateTime.now())
//                                    .build());
//                        });
//
//        codeVersionRepository.findByBuildVersionIsBefore(newestCodeVersion.buildVersion()).forEach(codeVersion -> {
//            codeVersionRepository.save(codeVersion.toBuilder()
//                    .mustUpdateApp(true)
//                    .build());
//        });
//    }
//    public void registerFoodCode(RegisterFoodCodeVo registerFoodCodeVo, String buildVersion) {
//        foodCodeRepository.save(FoodCode.builder()
//                .code(registerFoodCodeVo.code())
//                .name(registerFoodCodeVo.name())
//                .groupCode(registerFoodCodeVo.groupCode())
//                .price(registerFoodCodeVo.price())
//                .addWeightValue(registerFoodCodeVo.addWeightValue())
//                .addStrengthValue(registerFoodCodeVo.addStrengthValue())
//                .addSatietyValue(registerFoodCodeVo.addSatietyValue())
//                .addHealthyValue(registerFoodCodeVo.addHealthyValue())
//                .addSleepValue(registerFoodCodeVo.addSleepValue())
//                .buildVersion(buildVersion)
//                .build());
//        this.modifyMustUpdateApp(buildVersion);
//    }
//    public void registerMapCode(RegisterMapCodeVo registerMapCodeVo, String buildVersion) {
//        mapCodeRepository.save(MapCode.builder()
//                .code(registerMapCodeVo.code())
//                .name(registerMapCodeVo.name())
//                .buildVersion(buildVersion)
//                .build());
//        this.modifyMustUpdateApp(buildVersion);
//    }
//    public void registerMongCode(RegisterMongCodeVo registerMongCodeVo, String buildVersion) {
//        mongCodeRepository.save(MongCode.builder()
//                .code(registerMongCodeVo.code())
//                .name(registerMongCodeVo.name())
//                .buildVersion(buildVersion)
//                .build());
//        this.modifyMustUpdateApp(buildVersion);
//    }
//    public void registerFeedbackCode(RegisterFeedbackCodeVo registerFeedbackCodeVo, String buildVersion) {
//        feedbackCodeRepository.save(FeedbackCode.builder()
//                .code(registerFeedbackCodeVo.code())
//                .groupCode(registerFeedbackCodeVo.groupCode())
//                .message(registerFeedbackCodeVo.message())
//                .buildVersion(buildVersion)
//                .build());
//    }
//    public void initializeCode() {
//        String buildVersion = "1.0.0";
//
//        foodCodeRepository.deleteAll();
//        Arrays.stream(InitFoodCodeData.values()).forEach(foodCode -> {
//            foodCodeRepository.save(FoodCode.builder()
//                    .buildVersion(buildVersion)
//                    .code(foodCode.getCode())
//                    .name(foodCode.getName())
//                    .groupCode(foodCode.getGroupCode())
//                    .addHealthyValue(foodCode.getAddHealthyValue())
//                    .addSleepValue(foodCode.getAddSleepValue())
//                    .addSatietyValue(foodCode.getAddSatietyValue())
//                    .addWeightValue(foodCode.getAddWeightValue())
//                    .addStrengthValue(foodCode.getAddStrengthValue())
//                    .price(foodCode.getPrice())
//                    .build());
//        });
//
//        mapCodeRepository.deleteAll();
//        Arrays.stream(InitMapCodeData.values()).forEach(mapCode -> {
//            mapCodeRepository.save(MapCode.builder()
//                    .buildVersion(buildVersion)
//                    .code(mapCode.getCode())
//                    .name(mapCode.getName())
//                    .build());
//        });
//
//        mongCodeRepository.deleteAll();
//        Arrays.stream(InitMongCodeData.values()).forEach(mongCode -> {
//            mongCodeRepository.save(MongCode.builder()
//                    .buildVersion(buildVersion)
//                    .code(mongCode.getCode())
//                    .name(mongCode.getName())
//                    .build());
//        });
//
//        feedbackCodeRepository.deleteAll();
//        Arrays.stream(InitFeedbackCodeData.values()).forEach(feedbackCode -> {
//            feedbackCodeRepository.save(FeedbackCode.builder()
//                    .buildVersion(buildVersion)
//                    .code(feedbackCode.getCode())
//                    .groupCode(feedbackCode.getGroupCode())
//                    .message(feedbackCode.getMessage())
//                    .build());
//        });
//
//        String codeIntegrity = hmacProvider.generateHmac(FindCodeResDto.builder()
//                    .mapCodeList(this.findMapCode(buildVersion))
//                    .mongCodeList(this.findMongCode(buildVersion))
//                    .foodCodeList(this.findFoodCode(buildVersion))
//                    .feedbackCodeList(this.findFeedbackCode(buildVersion))
//                    .build())
//                .orElseThrow(RuntimeException::new);
//
//        codeVersionRepository.deleteAll();
//        codeVersionRepository.save(CodeVersion.builder()
//                .buildVersion(buildVersion)
//                .codeIntegrity(codeIntegrity)
//                .mustUpdateApp(false)
//                .createdAt(LocalDateTime.now())
//                .build());
//    }
}
