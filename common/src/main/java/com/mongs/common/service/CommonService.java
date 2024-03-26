package com.mongs.common.service;

import com.mongs.common.controller.dto.response.FindFeedbackCodeResDto;
import com.mongs.common.controller.dto.response.FindFoodCodeResDto;
import com.mongs.common.controller.dto.response.FindMapCodeResDto;
import com.mongs.common.controller.dto.response.FindMongCodeResDto;
import com.mongs.common.entity.CodeVersion;
import com.mongs.common.exception.CommonErrorCode;
import com.mongs.common.exception.NotFoundVersionException;
import com.mongs.common.repository.*;
import com.mongs.common.vo.FindVersionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final CodeVersionRepository codeVersionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    public FindVersionVo findVersion(String buildVersion) {
        CodeVersion codeVersion = codeVersionRepository.findTopByOrderByBuildVersionDesc()
                .orElseThrow(() -> new NotFoundVersionException(CommonErrorCode.NOT_FOUND_VERSION));

        int clientVersionFirst = Integer.parseInt(buildVersion.split("\\.")[0]);
        int serverVersionFirst = Integer.parseInt(codeVersion.buildVersion().split("\\.")[0]);
        boolean mustUpdateApp = clientVersionFirst < serverVersionFirst;

        int clientVersionSecond = Integer.parseInt(buildVersion.split("\\.")[1]);
        int serverVersionSecond = Integer.parseInt(codeVersion.buildVersion().split("\\.")[1]);
        boolean mustUpdateCode = clientVersionSecond < serverVersionSecond;

        return FindVersionVo.builder()
                .newestBuildVersion(codeVersion.buildVersion())
                .createdAt(codeVersion.createdAt())
                .mustUpdateApp(mustUpdateApp)
                .mustUpdateCode(mustUpdateCode || mustUpdateApp)
                .build();
    }

    public List<FindMapCodeResDto> findMapCode() {
        return FindMapCodeResDto.toList(mapCodeRepository.findAll());
    }

    public List<FindMongCodeResDto> findMongCode() {
        return FindMongCodeResDto.toList(mongCodeRepository.findAll());
    }

    public List<FindFoodCodeResDto> findFoodCode() {
        return FindFoodCodeResDto.toList(foodCodeRepository.findAll());
    }

    public List<FindFeedbackCodeResDto> findFeedbackCode() {
        return FindFeedbackCodeResDto.toList(feedbackCodeRepository.findAll());
    }
}
