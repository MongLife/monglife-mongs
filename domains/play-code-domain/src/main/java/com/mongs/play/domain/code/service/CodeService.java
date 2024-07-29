package com.mongs.play.domain.code.service;

import com.mongs.play.core.error.domain.CodeErrorCode;
import com.mongs.play.core.exception.common.AlreadyExistException;
import com.mongs.play.core.exception.common.NotFoundException;
import com.mongs.play.domain.code.entity.FoodCode;
import com.mongs.play.domain.code.entity.MapCode;
import com.mongs.play.domain.code.entity.MongCode;
import com.mongs.play.domain.code.repository.FoodCodeRepository;
import com.mongs.play.domain.code.repository.MapCodeRepository;
import com.mongs.play.domain.code.repository.MongCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeService {

    private final MapCodeRepository mapCodeRepository;
    private final MongCodeRepository mongCodeRepository;
    private final FoodCodeRepository foodCodeRepository;

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public MapCode getMapCode(String mapCode) throws NotFoundException {
        return mapCodeRepository.findById(mapCode)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_MAP_CODE));
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public MongCode getMongCode(String mongCode) throws NotFoundException {
        return mongCodeRepository.findById(mongCode)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_MONG_CODE));
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MapCode> getMapCode() {
        return mapCodeRepository.findAll();
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MongCode> getMongCode() {
        return mongCodeRepository.findAll();
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<FoodCode> getFoodCode() {
        return foodCodeRepository.findAll();
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public FoodCode getFoodCode(String foodCode) throws NotFoundException {
        return foodCodeRepository.findById(foodCode)
                .orElseThrow(() -> new NotFoundException(CodeErrorCode.NOT_FOUND_FOOD_CODE));
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MapCode> getMapCodeByBuildVersion(String buildVersion) {
        return mapCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MongCode> getMongCodeByBuildVersion(String buildVersion) {
        return mongCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MongCode> getMongCodeByLevel(Integer level) {
        return mongCodeRepository.findByLevel(level);
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<MongCode> getMongCodeByLevelAndEvolutionPoint(Integer level, Integer evolutionPoint) {
        return mongCodeRepository.findByLevelAndEvolutionPointIsLessThanEqualOrderByEvolutionPointDesc(level, evolutionPoint);
    }

    @Transactional(transactionManager = "codeTransactionManager", readOnly = true)
    public List<FoodCode> getFoodCodeByBuildVersion(String buildVersion) {
        return foodCodeRepository.findByBuildVersionIsLessThanEqual(buildVersion);
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public MapCode addMapCode(MapCode mapCode) throws AlreadyExistException {

        if (mapCodeRepository.findById(mapCode.getCode()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_MAP_CODE);
        }

        return mapCodeRepository.save(mapCode);
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public MongCode addMongCode(MongCode mongCode) throws AlreadyExistException {

        if (mongCodeRepository.findById(mongCode.getCode()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_MONG_CODE);
        }

        return mongCodeRepository.save(mongCode);
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public FoodCode addFoodCode(FoodCode foodCode) throws AlreadyExistException {

        if (foodCodeRepository.findById(foodCode.getCode()).isPresent()) {
            throw new AlreadyExistException(CodeErrorCode.ALREADY_EXIST_FOOD_CODE);
        }

        return foodCodeRepository.save(foodCode);
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public void removeMapCode() {
        mapCodeRepository.deleteAll();
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public void removeMongCode() {
        mongCodeRepository.deleteAll();
    }

    @Transactional(transactionManager = "codeTransactionManager")
    public void removeFoodCode() {
        foodCodeRepository.deleteAll();
    }
}
