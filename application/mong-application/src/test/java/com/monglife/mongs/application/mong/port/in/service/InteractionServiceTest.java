package com.monglife.mongs.application.mong.port.in.service;

import com.monglife.mongs.application.mong.port.exception.InvalidUseInventoryItemException;
import com.monglife.mongs.application.mong.port.exception.NotExistsMongException;
import com.monglife.mongs.application.mong.port.in.InteractionUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.application.mong.port.out.MongEventPort;
import com.monglife.mongs.application.mong.port.out.MongPersistencePort;
import com.monglife.mongs.domain.enums.InventoryItemTypeCode;
import com.monglife.mongs.domain.exception.InvalidMongStateException;
import com.monglife.mongs.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InteractionServiceTest {

    private final MongPersistencePort mongPersistencePort;

    private final MongEventPort mongEventPort;

    private final InteractionUseCase interactionUseCase;

    public InteractionServiceTest() {
        this.mongPersistencePort = Mockito.mock(MongPersistencePort.class);
        this.mongEventPort = Mockito.mock(MongEventPort.class);
        this.interactionUseCase = new InteractionService(mongPersistencePort, mongEventPort);
    }

    @Nested
    @DisplayName("음식 목록 조회 단위 테스트")
    class GetFoodsUseCase {

        @Test
        @DisplayName("섭취 가능 여부를 확인할 수 있는 음식 목록을 조회 한다.")
        void getFoods() {
            // arrange
            int price = 100;
            boolean isCanBuy = true;
            double status = 100;
            List<Food> foods = List.of(MongTestUtil.getFood(price, isCanBuy, status));

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;

            Mockito.when(mongPersistencePort.getFoodsPort(mongId)).thenReturn(foods);
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus)));

            // act
            GetFoodsCommand command = GetFoodsCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            List<Food> expected = interactionUseCase.getFoodsUseCase(command);

            // assert
            assertIterableEquals(expected, foods);
        }

        @Test
        @DisplayName("몽이 존재하지 않는 경우 에외가 발생 한다.")
        void getFoodsWhenNotExistsMong() {
            // arrange
            int price = 100;
            boolean isCanBuy = true;
            double status = 100;
            List<Food> foods = List.of(MongTestUtil.getFood(price, isCanBuy, status));

            long mongId = 1L;
            long accountId = 1L;

            Mockito.when(mongPersistencePort.getFoodsPort(mongId)).thenReturn(foods);
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.empty());

            // act & assert
            GetFoodsCommand command = GetFoodsCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(NotExistsMongException.class, () -> interactionUseCase.getFoodsUseCase(command));
        }
    }

    @Nested
    @DisplayName("간식 목록 조회 단위 테스트")
    class GetSnacksUseCase {

        @Test
        @DisplayName("섭취 가능 여부를 확인할 수 있는 간식 목록을 조회 한다.")
        void getSnacks() {
            // arrange
            int price = 100;
            boolean isCanBuy = true;
            double status = 100;
            List<Snack> snacks = List.of(MongTestUtil.getSnack(price, isCanBuy, status));

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;

            Mockito.when(mongPersistencePort.getSnacksPort(mongId)).thenReturn(snacks);
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus)));

            // act
            GetSnacksCommand command = GetSnacksCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            List<Snack> expected = interactionUseCase.getSnacksUseCase(command);

            // assert
            assertIterableEquals(expected, snacks);
        }

        @Test
        @DisplayName("몽이 존재하지 않는 경우 에외가 발생 한다.")
        void getFoodsWhenNotExistsMong() {
            // arrange
            int price = 100;
            boolean isCanBuy = true;
            double status = 100;
            List<Snack> snacks = List.of(MongTestUtil.getSnack(price, isCanBuy, status));

            long mongId = 1L;
            long accountId = 1L;

            Mockito.when(mongPersistencePort.getSnacksPort(mongId)).thenReturn(snacks);
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.empty());

            // act & assert
            GetSnacksCommand command = GetSnacksCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(NotExistsMongException.class, () -> interactionUseCase.getSnacksUseCase(command));
        }
    }

    @Nested
    @DisplayName("음식 섭취 단위 테스트")
    class FeedFoodUseCase {

        @Test
        @DisplayName("몽이 음식을 섭취 한다.")
        void feedFood() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Food food = MongTestUtil.getFood(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            Mockito.when(mongPersistencePort.getFoodPort(food.getFoodTypeCode(), mongId)).thenReturn(Optional.of(food));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act
            FeedFoodCommand command = FeedFoodCommand.builder()
                    .foodTypeCode(food.getFoodTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            Mong expected = interactionUseCase.feedFoodUseCase(command);

            // assert
            assertEquals(payPoint - price, expected.getPayPoint());
            assertEquals(status + food.getStrength(), expected.getStrength());
            assertEquals(status + food.getSatiety(), expected.getSatiety());
            assertEquals(status + food.getHealthy(), expected.getHealthy());
            assertEquals(status + food.getFatigue(), expected.getFatigue());
            assertEquals(status + food.getWeight(), expected.getWeight());
        }

        @Test
        @DisplayName("몽 레벨이 0인 경우 (알 상태인 경우) 예외가 발생 한다.")
        void feedFoodWhenEggMong() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Food food = MongTestUtil.getFood(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getEggMong(mongId, accountId, maxStatus);

            Mockito.when(mongPersistencePort.getFoodPort(food.getFoodTypeCode(), mongId)).thenReturn(Optional.of(food));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedFoodCommand command = FeedFoodCommand.builder()
                    .foodTypeCode(food.getFoodTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedFoodUseCase(command));
        }

        @Test
        @DisplayName("몽 자는 상태인 경우 예외가 발생 한다.")
        void feedFoodWhenMongIsSleeping() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Food food = MongTestUtil.getFood(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            // 자는 상태로 변경
            mong.sleep();

            Mockito.when(mongPersistencePort.getFoodPort(food.getFoodTypeCode(), mongId)).thenReturn(Optional.of(food));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedFoodCommand command = FeedFoodCommand.builder()
                    .foodTypeCode(food.getFoodTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedFoodUseCase(command));
        }

        @Test
        @DisplayName("몽 사망 상태인 경우 예외가 발생 한다.")
        void feedFoodWhenMongIsDead() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Food food = MongTestUtil.getFood(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            // 사망 상태로 변경
            mong.dead();

            Mockito.when(mongPersistencePort.getFoodPort(food.getFoodTypeCode(), mongId)).thenReturn(Optional.of(food));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedFoodCommand command = FeedFoodCommand.builder()
                    .foodTypeCode(food.getFoodTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedFoodUseCase(command));
        }
    }

    @Nested
    @DisplayName("간식 섭취 단위 테스트")
    class FeedSnackUseCase {

        @Test
        @DisplayName("몽이 간식을 섭취 한다.")
        void feedSnack() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Snack snack = MongTestUtil.getSnack(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            Mockito.when(mongPersistencePort.getSnackPort(snack.getSnackTypeCode(), mongId)).thenReturn(Optional.of(snack));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.saveMongPort(mong)).thenReturn(Optional.of(mong));

            // act
            FeedSnackCommand command = FeedSnackCommand.builder()
                    .snackTypeCode(snack.getSnackTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            Mong expected = interactionUseCase.feedSnackUseCase(command);

            // assert
            assertEquals(payPoint - price, expected.getPayPoint());
            assertEquals(status + snack.getStrength(), expected.getStrength());
            assertEquals(status + snack.getSatiety(), expected.getSatiety());
            assertEquals(status + snack.getHealthy(), expected.getHealthy());
            assertEquals(status + snack.getFatigue(), expected.getFatigue());
            assertEquals(status + snack.getWeight(), expected.getWeight());
        }

        @Test
        @DisplayName("몽 레벨이 0인 경우 (알 상태인 경우) 예외가 발생 한다.")
        void feedFoodWhenEggMong() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Snack snack = MongTestUtil.getSnack(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getEggMong(mongId, accountId, maxStatus);

            Mockito.when(mongPersistencePort.getSnackPort(snack.getSnackTypeCode(), mongId)).thenReturn(Optional.of(snack));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedSnackCommand command = FeedSnackCommand.builder()
                    .snackTypeCode(snack.getSnackTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedSnackUseCase(command));
        }

        @Test
        @DisplayName("몽 자는 상태인 경우 예외가 발생 한다.")
        void feedFoodWhenMongIsSleeping() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Snack snack = MongTestUtil.getSnack(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            // 수면 상태로 변경
            mong.sleep();

            Mockito.when(mongPersistencePort.getSnackPort(snack.getSnackTypeCode(), mongId)).thenReturn(Optional.of(snack));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedSnackCommand command = FeedSnackCommand.builder()
                    .snackTypeCode(snack.getSnackTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedSnackUseCase(command));
        }

        @Test
        @DisplayName("몽 사망 상태인 경우 예외가 발생 한다.")
        void feedFoodWhenMongIsDead() {
            // arrange
            int price = 50;
            boolean isCanBuy = true;
            Snack snack = MongTestUtil.getSnack(price, isCanBuy, 10D);

            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            // 사망 상태로 변경
            mong.dead();

            Mockito.when(mongPersistencePort.getSnackPort(snack.getSnackTypeCode(), mongId)).thenReturn(Optional.of(snack));
            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));

            // act & assert
            FeedSnackCommand command = FeedSnackCommand.builder()
                    .snackTypeCode(snack.getSnackTypeCode())
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidMongStateException.class, () -> interactionUseCase.feedSnackUseCase(command));
        }
    }

    @Nested
    @DisplayName("인벤 아이템 목록 조회 단위 테스트")
    class GetInventoryItemsUseCase {

        @Test
        @DisplayName("인벤토리 아이템 목록을 조회 한다.")
        void getInventoryItems() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, maxStatus);

            long inventoryItemId = 1L;
            String typeCode = "TEST-INVENTORY-ITEM-TYPE-CODE";
            String typeName = "TEST-INVENTORY-ITEM-TYPE-NAME";

            List<InventoryItem> inventoryItems = List.of(
                    InventoryItem.builder()
                            .inventoryItemId(inventoryItemId)
                            .mongId(mongId)
                            .inventoryItemTypeCode(InventoryItemTypeCode.FOOD)
                            .typeCode(typeCode)
                            .typeName(typeName)
                            .build());

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getInventoryItemsPort(mongId)).thenReturn(inventoryItems);

            // act
            GetInventoryItemsCommand command = GetInventoryItemsCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            List<InventoryItem> expected = interactionUseCase.getInventoryItemsUseCase(command);

            // assert
            assertIterableEquals(expected, inventoryItems);
        }
    }

    @Nested
    @DisplayName("인벤 소비성 아이템 사용 단위 테스트")
    class UseInventoryItemUseCase {

        @Test
        @DisplayName("소비성 인벤토리 아이템을 사용하여 음식을 섭취 한다.")
        void useInventoryItemWhenFeedFood() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            int price = 50;
            boolean isCanBuy = true;
            Food food = MongTestUtil.getFood(price, isCanBuy, 10D);

            long inventoryItemId = 1L;
            InventoryItem inventoryItem = InventoryItem.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .inventoryItemTypeCode(InventoryItemTypeCode.FOOD)
                    .typeCode(food.getFoodTypeCode())
                    .typeName(food.getFoodTypeName())
                    .build();

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getInventoryItemPort(mongId)).thenReturn(Optional.of(inventoryItem));
            Mockito.when(mongPersistencePort.getFoodPort(food.getFoodTypeCode(), mongId)).thenReturn(Optional.of(food));
            Mockito.when((mongPersistencePort.saveMongPort(mong))).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.deleteInventoryItemPort(inventoryItemId)).thenReturn(Optional.of(inventoryItem));

            // act
            UseInventoryItemCommand command = UseInventoryItemCommand.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            Mong expected = interactionUseCase.useInventoryItemUseCase(command);

            // assert
            assertEquals(payPoint, mong.getPayPoint());
            assertEquals(status + food.getStrength(), expected.getStrength());
            assertEquals(status + food.getSatiety(), expected.getSatiety());
            assertEquals(status + food.getHealthy(), expected.getHealthy());
            assertEquals(status + food.getFatigue(), expected.getFatigue());
            assertEquals(status + food.getWeight(), expected.getWeight());
        }

        @Test
        @DisplayName("소비성 인벤토리 아이템을 사용하여 간식을 섭취 한다.")
        void useInventoryItemWhenFeedSnack() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            int price = 50;
            boolean isCanBuy = true;
            Snack snack = MongTestUtil.getSnack(price, isCanBuy, 10D);

            long inventoryItemId = 1L;
            InventoryItem inventoryItem = InventoryItem.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .inventoryItemTypeCode(InventoryItemTypeCode.SNACK)
                    .typeCode(snack.getSnackTypeCode())
                    .typeName(snack.getSnackTypeName())
                    .build();

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getInventoryItemPort(mongId)).thenReturn(Optional.of(inventoryItem));
            Mockito.when(mongPersistencePort.getSnackPort(snack.getSnackTypeCode(), mongId)).thenReturn(Optional.of(snack));
            Mockito.when(mongPersistencePort.saveMongPort(mong)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.deleteInventoryItemPort(inventoryItemId)).thenReturn(Optional.of(inventoryItem));

            // act
            UseInventoryItemCommand command = UseInventoryItemCommand.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            Mong expected = interactionUseCase.useInventoryItemUseCase(command);

            // assert
            assertEquals(payPoint, mong.getPayPoint());
            assertEquals(status + snack.getStrength(), expected.getStrength());
            assertEquals(status + snack.getSatiety(), expected.getSatiety());
            assertEquals(status + snack.getHealthy(), expected.getHealthy());
            assertEquals(status + snack.getFatigue(), expected.getFatigue());
            assertEquals(status + snack.getWeight(), expected.getWeight());
        }

        @Test
        @DisplayName("소비성 아이템이 아닌 경우 예외가 발생 한다.")
        void useInventoryWhenNotConsumedInventoryItem() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            long inventoryItemId = 1L;
            String typeCode = "TEST-TYPE-CODE";
            String typeName = "TEST-TYPE-NAME";
            InventoryItem inventoryItem = InventoryItem.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .inventoryItemTypeCode(InventoryItemTypeCode.MAP)
                    .typeCode(typeCode)
                    .typeName(typeName)
                    .build();

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getInventoryItemPort(mongId)).thenReturn(Optional.of(inventoryItem));
            Mockito.when(mongPersistencePort.getInventoryItemsPort(mongId)).thenReturn(Collections.emptyList());

            // act & assert
            UseInventoryItemCommand command = UseInventoryItemCommand.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            assertThrows(InvalidUseInventoryItemException.class, () -> interactionUseCase.useInventoryItemUseCase(command));
        }
    }

    @Nested
    @DisplayName("랜덤 뽑기 단위 테스트")
    class RandomDrawUseCase {

        @Test
        @DisplayName("랜덤 뽑기를 통해 소비성 아이템을 뽑고 인벤토리 아이템으로 등록 한다.")
        void randomDraw() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            long inventoryItemId = 1L;
            String typeCode = "TEST-TYPE-CODE";
            String typeName = "TEST-TYPE-NAME";
            InventoryItem inventoryItem = InventoryItem.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .inventoryItemTypeCode(InventoryItemTypeCode.FOOD)
                    .typeCode(typeCode)
                    .typeName(typeName)
                    .build();

            List<RandomDrawItem> randomDrawItems = List.of(
                    RandomDrawItem.builder()
                            .randomDrawItemId(inventoryItemId)
                            .inventoryItemTypeCode(inventoryItem.getInventoryItemTypeCode())
                            .typeCode(inventoryItem.getTypeCode())
                            .typeName(inventoryItem.getTypeName())
                            .build());

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getRandomDrawItemsPort()).thenReturn(randomDrawItems);
            Mockito.when(mongPersistencePort.createInventoryItemPort(Mockito.any())).thenReturn(Optional.of(inventoryItem));

            // act
            RandomDrawCommand command = RandomDrawCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            RandomDrawItem exception = interactionUseCase.randomDrawUseCase(command);

            // assert
            assertEquals(inventoryItemId, exception.getRandomDrawItemId());
            assertEquals(typeCode, exception.getTypeCode());
            assertEquals(typeName, exception.getTypeName());
        }

        @Test
        @DisplayName("랜덤 뽑기를 통해 맵 아이템을 뽑고 컬렉션 맵 등록 이벤트를 발생 시킨다.")
        void randomDrawWhenDrawMap() {
            // arrange
            long mongId = 1L;
            long accountId = 1L;
            double maxStatus = 100;
            double status = 50;
            int payPoint = 100;
            Mong mong = MongTestUtil.getFirstLevelMong(mongId, accountId, status, maxStatus, payPoint);

            long inventoryItemId = 1L;
            String typeCode = "TEST-TYPE-CODE";
            String typeName = "TEST-TYPE-NAME";
            InventoryItem inventoryItem = InventoryItem.builder()
                    .inventoryItemId(inventoryItemId)
                    .mongId(mongId)
                    .inventoryItemTypeCode(InventoryItemTypeCode.MAP)
                    .typeCode(typeCode)
                    .typeName(typeName)
                    .build();

            List<RandomDrawItem> randomDrawItems = List.of(
                    RandomDrawItem.builder()
                            .randomDrawItemId(inventoryItemId)
                            .inventoryItemTypeCode(inventoryItem.getInventoryItemTypeCode())
                            .typeCode(inventoryItem.getTypeCode())
                            .typeName(inventoryItem.getTypeName())
                            .build());

            Mockito.when(mongPersistencePort.getMongPort(mongId)).thenReturn(Optional.of(mong));
            Mockito.when(mongPersistencePort.getRandomDrawItemsPort()).thenReturn(randomDrawItems);

            // act
            RandomDrawCommand command = RandomDrawCommand.builder()
                    .mongId(mongId)
                    .accountId(accountId)
                    .build();

            RandomDrawItem exception = interactionUseCase.randomDrawUseCase(command);

            // assert
            Mockito.verify(mongEventPort).randomDrawMapEventPort(accountId, typeCode);
            assertEquals(inventoryItemId, exception.getRandomDrawItemId());
            assertEquals(typeCode, exception.getTypeCode());
            assertEquals(typeName, exception.getTypeName());

        }
    }
}