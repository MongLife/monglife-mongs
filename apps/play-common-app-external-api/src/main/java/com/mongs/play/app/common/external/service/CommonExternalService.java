package com.mongs.play.app.common.external.service;

import com.mongs.play.app.common.external.vo.*;
import com.mongs.play.domain.code.entity.CodeVersion;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.service.CodeService;
import com.mongs.play.domain.code.service.CodeVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonExternalService {

    private final CodeVersionService codeVersionService;
    private final CodeService codeService;

    @Transactional(readOnly = true)
    public FindCodeVersionVo findCodeVersion(String buildVersion, String codeIntegrity) {

        CodeVersion codeVersion = codeVersionService.getCodeVersion(buildVersion);

        return FindCodeVersionVo.builder()
                .newestBuildVersion(codeVersion.getBuildVersion())
                .createdAt(codeVersion.getCreatedAt())
                .updateApp(codeVersion.getMustUpdateApp())                             /* 리소스가 추가되어 이전 버전들에 대한 앱 업데이트 여부 확인 */
                .updateCode(!codeVersion.getCodeIntegrity().equals(codeIntegrity))     /* 해싱 값이 다르면 코드 업데이트 */
                .build();
    }

    @Transactional(readOnly = true)
    public FindCodeVo findCode(String buildVersion) {

        CodeVersion codeVersion = codeVersionService.getCodeVersion(buildVersion);

        List<MapCode> mapCodeList = codeService.getMapCodeByBuildVersion(buildVersion);
        List<MongCode> mongCodeList = codeService.getMongCodeByBuildVersion(buildVersion);
        List<FoodCode> foodCodeList = codeService.getFoodCodeByBuildVersion(buildVersion);

        return FindCodeVo.builder()
                .codeIntegrity(codeVersion.getCodeIntegrity())
                .mapCodeList(FindMapCodeVo.toList(mapCodeList))
                .mongCodeList(FindMongCodeVo.toList(mongCodeList))
                .foodCodeList(FindFoodCodeVo.toList(foodCodeList))
                .build();
    }
}
