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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CodeVersionRepository codeVersionRepository;
    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    public FindVersionVo findVersion() {
        CodeVersion codeVersion = codeVersionRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new NotFoundVersionException(CommonErrorCode.NOT_FOUND_VERSION));

        return FindVersionVo.builder()
                .version(codeVersion.version())
                .createdAt(codeVersion.createdAt())
                .build();
    }

    public List<FindMapCodeResDto> findMapCode(Long version) {
        return FindMapCodeResDto.toList(mapCodeRepository.findByVersionIsAfter(version));
    }

    public List<FindMongCodeResDto> findMongCode(Long version) {
        return FindMongCodeResDto.toList(mongCodeRepository.findByVersionIsAfter(version));
    }

    public List<FindFoodCodeResDto> findFoodCode(Long version) {
        return FindFoodCodeResDto.toList(foodCodeRepository.findByVersionIsAfter(version));
    }

    public List<FindFeedbackCodeResDto> findFeedbackCode(Long version) {
        return FindFeedbackCodeResDto.toList(feedbackCodeRepository.findByVersionIsAfter(version));
    }
}
