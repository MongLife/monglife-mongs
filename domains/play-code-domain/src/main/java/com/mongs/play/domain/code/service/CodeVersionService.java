package com.mongs.play.domain.code.service;

import com.mongs.play.core.error.domain.CodeErrorCode;
import com.mongs.play.core.error.module.HmacErrorCode;
import com.mongs.play.core.exception.domain.GenerateException;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.code.entity.*;
import com.mongs.play.domain.code.repository.*;
import com.mongs.play.domain.code.vo.CodeVo;
import com.mongs.play.module.hmac.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeVersionService {

    private final HmacProvider hmacProvider;
    private final CodeVersionRepository codeVersionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    public CodeVersion addCodeVersion(String buildVersion) {

        codeVersionRepository.findByBuildVersion(buildVersion)
                .ifPresent(codeVersion -> codeVersionRepository.deleteById(buildVersion));

        return codeVersionRepository.save(CodeVersion.builder()
                .buildVersion(buildVersion)
                .codeIntegrity(this.getIntegrity(buildVersion))
                .mustUpdateApp(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public CodeVersion getCodeVersion(String buildVersion) throws NotFoundException {
        return codeVersionRepository.findByBuildVersion(buildVersion)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_CODE_VERSION));
    }

    @Transactional
    public CodeVersion updateCodeWithUpdateApp(String buildVersion) throws NotFoundException, GenerateException {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_CODE_VERSION));

        codeVersionRepository.save(codeVersion.toBuilder()
                .codeIntegrity(this.getIntegrity(buildVersion))
                .build());

        /* 이전 버전들에 대해 업데이트 여부 true 로 변경 */
        List<CodeVersion> pastCodeVersionList = codeVersionRepository.findByBuildVersionIsBefore(codeVersion.buildVersion());

        pastCodeVersionList.forEach(pastCodeVersion -> {
            codeVersionRepository.save(pastCodeVersion.toBuilder()
                    .mustUpdateApp(true)
                    .build());
        });

        return codeVersion;
    }

    @Transactional
    public CodeVersion updateCode(String buildVersion) throws NotFoundException, GenerateException {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                        .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_CODE_VERSION));

        codeVersionRepository.save(codeVersion.toBuilder()
                .codeIntegrity(this.getIntegrity(buildVersion))
                .build());

        return codeVersion;
    }

    private String getIntegrity(String buildVersion) throws GenerateException {

        List<MapCode> mapCodeList = mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<MongCode> mongCodeList = mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<FoodCode> foodCodeList = foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<FeedbackCode> feedbackCodeList = feedbackCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);

        return hmacProvider.generateHmac(CodeVo.builder()
                        .mapCodeList(mapCodeList)
                        .mongCodeList(mongCodeList)
                        .foodCodeList(foodCodeList)
                        .feedbackCodeList(feedbackCodeList)
                        .build())
                .orElseThrow(() -> new GenerateException(HmacErrorCode.GENERATE_HMAC));
    }

    public void removeCodeVersion() {
        codeVersionRepository.deleteAll();
    }
}
