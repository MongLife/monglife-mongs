package com.mongs.common.service;

import com.mongs.common.dto.response.FindFeedbackCodeResDto;
import com.mongs.common.dto.response.FindFoodCodeResDto;
import com.mongs.common.dto.response.FindMapCodeResDto;
import com.mongs.common.dto.response.FindMongCodeResDto;
import com.mongs.common.entity.CodeVersion;
import com.mongs.common.exception.CommonErrorCode;
import com.mongs.common.exception.NewestVersionException;
import com.mongs.common.exception.NotFoundVersionException;
import com.mongs.common.repository.*;
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

    public String codeVersionCheckAndNewestCode(String version) {
        CodeVersion newestVersion = codeVersionRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new NotFoundVersionException(CommonErrorCode.NOT_FOUND_VERSION));

        if (version.equals(newestVersion.version())) {
            throw new NewestVersionException(CommonErrorCode.ALREADY_NEW_VERSION);
        }

        return newestVersion.version();
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
