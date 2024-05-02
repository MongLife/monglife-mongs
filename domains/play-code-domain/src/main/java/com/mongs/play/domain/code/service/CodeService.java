package com.mongs.play.domain.code.service;

import com.mongs.play.core.error.domain.CodeErrorCode;
import com.mongs.play.core.exception.domain.AlreadyExistException;
import com.mongs.play.core.exception.domain.NotFoundException;
import com.mongs.play.domain.code.entity.*;
import com.mongs.play.domain.code.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;
    private final FeedbackCodeRepository feedbackCodeRepository;

    public MapCode getMapCode(String mapCode) throws NotFoundException {
        return mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_MAP_CODE));
    }

    public MongCode getMongCode(String mongCode) throws NotFoundException {
        return mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_MONG_CODE));
    }

    public List<MapCode> getMapCode() {
        return mapCodeRepository.findAll();
    }

    public List<MongCode> getMongCode() {
        return mongCodeRepository.findAll();
    }

    public List<FoodCode> getFoodCode() {
        return foodCodeRepository.findAll();
    }

    public List<FeedbackCode> getFeedbackCode() {
        return feedbackCodeRepository.findAll();
    }

    public List<MapCode> getMapCodeByBuildVersion(String buildVersion) {
        return mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    public List<MongCode> getMongCodeByBuildVersion(String buildVersion) {
        return mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    public List<FoodCode> getFoodCodeByBuildVersion(String buildVersion) {
        return foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    public List<FeedbackCode> getFeedbackCodeByBuildVersion(String buildVersion) {
        return feedbackCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    public MapCode addMapCode(MapCode mapCode) {

        if (mapCodeRepository.findById(mapCode.code()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_MAP_CODE);
        }

        return mapCodeRepository.save(mapCode);
    }

    public MongCode addMongCode(MongCode mongCode) {

        if (mongCodeRepository.findById(mongCode.code()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_MONG_CODE);
        }

        return mongCodeRepository.save(mongCode);
    }

    public FoodCode addFoodCode(FoodCode foodCode) {

        if (foodCodeRepository.findById(foodCode.code()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_FOOD_CODE);
        }

        return foodCodeRepository.save(foodCode);
    }

    public FeedbackCode addFeedbackCode(FeedbackCode feedbackCode) {

        if (feedbackCodeRepository.findById(feedbackCode.code()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_FEEDBACK_CODE);
        }

        return feedbackCodeRepository.save(feedbackCode);
    }

    public void removeMapCode() {
        mapCodeRepository.deleteAll();
    }

    public void removeMongCode() {
        mongCodeRepository.deleteAll();
    }

    public void removeFoodCode() {
        foodCodeRepository.deleteAll();
    }

    public void removeFeedbackCode() {
        feedbackCodeRepository.deleteAll();
    }
}
