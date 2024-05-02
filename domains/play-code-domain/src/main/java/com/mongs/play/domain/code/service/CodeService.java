package com.mongs.play.domain.code.service;

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
        return mapCodeRepository.save(mapCode);
    }

    public MongCode addMongCode(MongCode mongCode) {
        return mongCodeRepository.save(mongCode);
    }

    public FoodCode addFoodCode(FoodCode foodCode) {
        return foodCodeRepository.save(foodCode);
    }

    public FeedbackCode addFeedbackCode(FeedbackCode feedbackCode) {
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
