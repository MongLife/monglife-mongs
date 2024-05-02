package com.mongs.play.domain.code.service;

import com.mongs.play.domain.code.entity.*;
import com.mongs.play.domain.code.repository.*;
import com.mongs.play.domain.code.vo.CodeVo;
import com.mongs.play.hmac.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<CodeVersion> getCodeVersion(String buildVersion) {
        return codeVersionRepository.findByBuildVersion(buildVersion);
    }

    public void updateCodeWithUpdateApp(String buildVersion) {
        CodeVersion codeVersion = this.updateCode(buildVersion);

        /* 이전 버전들에 대해 업데이트 여부 true 로 변경 */
        List<CodeVersion> pastCodeVersionList = codeVersionRepository.findByBuildVersionIsBefore(codeVersion.buildVersion());

        pastCodeVersionList.forEach(pastCodeVersion -> {
            codeVersionRepository.save(pastCodeVersion.toBuilder()
                    .mustUpdateApp(true)
                    .build());
        });
    }

    public CodeVersion updateCode(String buildVersion) {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersion(buildVersion)
                .orElseGet(() -> this.addCodeVersion(buildVersion));

        codeVersionRepository.save(codeVersion.toBuilder()
                .codeIntegrity(this.getIntegrity(buildVersion))
                .build());

        return codeVersion;
    }

    private String getIntegrity(String buildVersion) {

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
                .orElse("");
    }

    public void removeCodeVersion() {
        codeVersionRepository.deleteAll();
    }
}
