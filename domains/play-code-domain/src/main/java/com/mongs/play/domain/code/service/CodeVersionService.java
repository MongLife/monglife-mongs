package com.mongs.play.domain.code.service;

import com.mongs.play.core.error.domain.CodeErrorCode;
import com.mongs.play.core.error.module.HmacErrorCode;
import com.mongs.play.core.exception.common.GenerateException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.code.entity.CodeVersion;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.repository.FoodCodeRepository;
import com.mongs.play.domain.code.vo.CodeVo;
import com.mongs.play.domain.code.repository.CodeVersionRepository;
import com.mongs.play.domain.code.repository.MapCodeRepository;
import com.mongs.play.domain.code.repository.MongCodeRepository;
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

    @Transactional(transactionManager = "codeTransactionManager")
    public CodeVersion addCodeVersion(String buildVersion) {

        codeVersionRepository.findByBuildVersionWithLock(buildVersion)
                .ifPresent(codeVersion -> codeVersionRepository.deleteById(buildVersion));

        List<MapCode> mapCodeList = mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<MongCode> mongCodeList = mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<FoodCode> foodCodeList = foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);

        return codeVersionRepository.save(CodeVersion.builder()
                .buildVersion(buildVersion)
                .codeIntegrity(this.getIntegrity(mapCodeList, mongCodeList, foodCodeList))
                .mustUpdateApp(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public CodeVersion getCodeVersion(String buildVersion) throws NotFoundException {
        return codeVersionRepository.findByBuildVersion(buildVersion)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_CODE_VERSION));
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public CodeVersion updateCode(String buildVersion) throws NotFoundException, GenerateException {
        CodeVersion codeVersion = codeVersionRepository.findByBuildVersionWithLock(buildVersion)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_CODE_VERSION));

        List<MapCode> mapCodeList = mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<MongCode> mongCodeList = mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
        List<FoodCode> foodCodeList = foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);

        codeVersionRepository.save(codeVersion.toBuilder()
                .codeIntegrity(this.getIntegrity(mapCodeList, mongCodeList, foodCodeList))
                .build());

        /* 이전 버전들에 대해 업데이트 여부 true 로 변경 */
        List<CodeVersion> pastCodeVersionList = codeVersionRepository.findByBuildVersionIsBefore(codeVersion.getBuildVersion());

        pastCodeVersionList.forEach(pastCodeVersion -> {
            codeVersionRepository.save(pastCodeVersion.toBuilder()
                    .mustUpdateApp(true)
                    .build());
        });

        return codeVersion;
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public void removeCodeVersion() {
        codeVersionRepository.deleteAll();
    }

    private String getIntegrity(List<MapCode> mapCodeList, List<MongCode> mongCodeList, List<FoodCode> foodCodeList) throws GenerateException {
        return hmacProvider.generateHmac(CodeVo.builder()
                        .mapCodeList(mapCodeList)
                        .mongCodeList(mongCodeList)
                        .foodCodeList(foodCodeList)
                        .build())
                .orElseThrow(() -> new GenerateException(HmacErrorCode.GENERATE_HMAC));
    }
}
