package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.annotation.PublishMongPort;
import com.monglife.mongs.application.mong.port.exception.*;
import com.monglife.mongs.application.mong.port.in.InteractionUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.application.mong.port.out.vo.CreateInventoryItemVo;
import com.monglife.mongs.domain.exception.ForbiddenInventoryItemException;
import com.monglife.mongs.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InteractionService implements InteractionUseCase {

    private static final Random random = new Random();

    private final MongPersistencePort mongPersistencePort;

    private final MongEventPort mongEventPort;

    /**
     * 음식 목록 조회
     */
    @Override
    @Transactional
    public List<Food> getFoodsUseCase(GetFoodsCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        return mongPersistencePort.getFoodsPort(mong.getMongId());
    }

    /**
     * 간식 목록 조회
     */
    @Override
    @Transactional
    public List<Snack> getSnacksUseCase(GetSnacksCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        return mongPersistencePort.getSnacksPort(mong.getMongId());
    }

    /**
     * 음식 섭취
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong feedFoodUseCase(FeedFoodCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 음식 조회
        Food food = mongPersistencePort.getFoodPort(command.getFoodTypeCode(), mong.getMongId())
                .orElseThrow(NotExistsFoodException::new);

        // 음식 섭취
        mong.feedWithBuy(food);

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 간식 섭취
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong feedSnackUseCase(FeedSnackCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 간식 조회
        Snack snack = mongPersistencePort.getSnackPort(command.getSnackTypeCode(), mong.getMongId())
                .orElseThrow(NotExistsSnackException::new);

        // 간식 섭취
        mong.feedWithBuy(snack);

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        return mong;
    }

    /**
     * 인벤 아이템 목록 조회
     */
    @Override
    @Transactional
    public List<InventoryItem> getInventoryItemsUseCase(GetInventoryItemsCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        return mongPersistencePort.getInventoryItemsPort(mong.getMongId());
    }

    /**
     * 인벤 소비성 아이템 사용
     */
    @Override
    @Transactional
    @PublishMongPort
    public Mong useInventoryItemUseCase(UseInventoryItemCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        InventoryItem inventoryItem = mongPersistencePort.getInventoryItemPort(command.getInventoryItemId())
                .orElseThrow(NotExistsInventoryItemException::new)
                .verify(command.getMongId());

        // 인벤토리 아이템 권한 체크
        if (!mong.getMongId().equals(inventoryItem.getMongId())) {
            throw new ForbiddenInventoryItemException();
        }

        switch (inventoryItem.getInventoryItemTypeCode()) {
            // 음식 섭취
            case FOOD -> mong.feed(mongPersistencePort.getFoodPort(inventoryItem.getTypeCode(), mong.getMongId())
                    .orElseThrow(NotExistsFoodException::new));
            // 간식 섭취
            case SNACK -> mong.feed(mongPersistencePort.getSnackPort(inventoryItem.getTypeCode(), mong.getMongId())
                    .orElseThrow(NotExistsSnackException::new));
            // 이외의 경우 예외
            default -> throw new InvalidUseInventoryItemException();
        }

        // 몽 정보 동기화
        mong = mongPersistencePort.saveMongPort(mong)
                .orElseThrow(NotExistsMongException::new);

        // 인벤토리 아이템 삭제
        mongPersistencePort.deleteInventoryItemPort(inventoryItem.getInventoryItemId())
                .orElseThrow(InvalidDeleteInventoryItemException::new);

        return mong;
    }

    /**
     * 랜덤 뽑기
     */
    @Override
    @Transactional
    public RandomDrawItem randomDrawUseCase(RandomDrawCommand command) {

        Mong mong = mongPersistencePort.getMongPort(command.getMongId())
                .orElseThrow(NotExistsMongException::new)
                .verify(command.getAccountId());

        // 랜덤 뽑기 횟수 차감
        mong.decreaseRandomDrawTicketCount();

        // 랜덤 뽑기 아이템 목록 조회
        List<RandomDrawItem> randomDrawItems = mongPersistencePort.getRandomDrawItemsPort();

        // 랜덤 뽑기 아이템 목록이 없는 경우 예외
        if (randomDrawItems.isEmpty()) {
            throw new NotExistsRandomDrawItemsException();
        }

        // 코드 값 랜덤 선정
        int randomDrawItemsIndex = random.nextInt(0, randomDrawItems.size());
        // 랜덤 뽑기 아이템 선정
        RandomDrawItem randomDrawItem = randomDrawItems.get(randomDrawItemsIndex);

        switch (randomDrawItem.getInventoryItemTypeCode()) {
            // 맵인 경우 컬렉션 맵 등록
            case MAP -> mongEventPort.randomDrawMapEventPort(command.getAccountId(), randomDrawItem.getTypeCode());
            // 음식, 간식인 경우 인벤토리 등록
            case FOOD, SNACK ->
                    mongPersistencePort.createInventoryItemPort(CreateInventoryItemVo.builder()
                        .mongId(mong.getMongId())
                        .typeCode(randomDrawItem.getTypeCode())
                        .inventoryItemTypeCode(randomDrawItem.getInventoryItemTypeCode())
                        .build())
                        .orElseThrow(InvalidCreateInventoryItemException::new);
        }

        return randomDrawItem;
    }
}
