package com.monglife.mongs.adapter.in.mong.web.controller;

import com.monglife.core.dto.response.PageResponseDto;
import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.vo.page.PageResult;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.mong.web.dto.request.FeedFoodRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.request.FeedSnackRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.request.UseInventoryRequestDto;
import com.monglife.mongs.adapter.in.mong.web.dto.response.*;
import com.monglife.mongs.adapter.in.mong.web.enums.AdapterInMongWebResponse;
import com.monglife.mongs.application.mong.port.in.InteractionUseCase;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.Inventory;
import com.monglife.mongs.domain.mong.model.Mong;
import com.monglife.mongs.domain.mong.model.RandomDraw;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/interaction")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionUseCase interactionUseCase;

    /**
     * 음식 목록 조회
     */
    @EntryLoggingPoint
    @GetMapping("/food/{mongId}")
    public ResponseEntity<ResponseDto<List<GetFoodResponseDto>>> getFoods(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        GetFoodsCommand command = GetFoodsCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        List<GetFoodResponseDto> getFoodResponseDtos = interactionUseCase.getFoodsUseCase(command).stream()
                .map(food -> GetFoodResponseDto.builder()
                        .foodCode(food.getFoodCode())
                        .foodName(food.getFoodName())
                        .price(food.getPrice())
                        .weight(food.getWeight())
                        .strength(food.getStrength())
                        .satiety(food.getSatiety())
                        .healthy(food.getHealthy())
                        .fatigue(food.getFatigue())
                        .isCanBuy(food.getIsCanBuy())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_FOODS.toResponseDto(getFoodResponseDtos));
    }

    /**
     * 간식 목록 조회
     */
    @EntryLoggingPoint
    @GetMapping("/snack/{mongId}")
    public ResponseEntity<ResponseDto<List<GetSnackResponseDto>>> getSnacks(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        GetSnacksCommand command = GetSnacksCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        List<GetSnackResponseDto> getSnackResponseDtos = interactionUseCase.getSnacksUseCase(command).stream()
                .map(snack -> GetSnackResponseDto.builder()
                        .snackCode(snack.getSnackCode())
                        .snackName(snack.getSnackName())
                        .price(snack.getPrice())
                        .weight(snack.getWeight())
                        .strength(snack.getStrength())
                        .satiety(snack.getSatiety())
                        .healthy(snack.getHealthy())
                        .fatigue(snack.getFatigue())
                        .isCanBuy(snack.getIsCanBuy())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInMongWebResponse.GET_SNACKS.toResponseDto(getSnackResponseDtos));
    }

    /**
     * 음식 섭취
     */
    @EntryLoggingPoint
    @PostMapping("/food/{mongId}")
    public ResponseEntity<ResponseDto<FeedFoodResponseDto>> feedFood(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId,
            @Valid @RequestBody FeedFoodRequestDto feedFoodRequestDto
    ) {
        FeedFoodCommand command = FeedFoodCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .foodCode(feedFoodRequestDto.getFoodCode())
                .build();

        Mong mong = interactionUseCase.feedFoodUseCase(command);

        FeedFoodResponseDto feedFoodResponseDto = FeedFoodResponseDto.builder()
                .mongId(mong.getMongId())
                .payPoint(mong.getPayPoint())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.FEED_FOOD.toResponseDto(feedFoodResponseDto));
    }

    /**
     * 간식 섭취
     */
    @EntryLoggingPoint
    @PostMapping("/snack/{mongId}")
    public ResponseEntity<ResponseDto<FeedSnackResponseDto>> feedSnack(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId,
            @Valid @RequestBody FeedSnackRequestDto feedSnackRequestDto
    ) {
        FeedSnackCommand command = FeedSnackCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .snackCode(feedSnackRequestDto.getSnackCode())
                .build();

        Mong mong = interactionUseCase.feedSnackUseCase(command);

        FeedSnackResponseDto feedSnackResponseDto = FeedSnackResponseDto.builder()
                .mongId(mong.getMongId())
                .payPoint(mong.getPayPoint())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.FEED_SNACK.toResponseDto(feedSnackResponseDto));
    }

    /**
     * 인벤토리 목록 조회
     */
    @EntryLoggingPoint
    @GetMapping("/inventory/{mongId}")
    public ResponseEntity<PageResponseDto<List<GetInventoryResponseDto>>> getInventories(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId,
            @RequestParam("page") @NotNull @Min(1) Integer page,
            @RequestParam("size") @NotNull @Min(1) @Max(10) Integer size
    ) {
        GetInventoriesCommand command = GetInventoriesCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .page(page)
                .size(size)
                .build();

        PageResult<Inventory> inventoriesPage = interactionUseCase.getInventoriesUseCase(command);

        List<GetInventoryResponseDto> getInventoryResponseDtos = inventoriesPage.getResult().stream()
                .map(inventory -> GetInventoryResponseDto.builder()
                        .mongId(inventory.getMongId())
                        .inventoryId(inventory.getInventoryId())
                        .inventoryCode(inventory.getInventoryCode())
                        .inventoryName(inventory.getInventoryName())
                        .inventoryTypeCode(inventory.getInventoryTypeCode())
                        .build())
                .toList();

        return ResponseEntity.ok(
                AdapterInMongWebResponse.GET_INVENTORIES.toPageResponseDto(
                        getInventoryResponseDtos,
                        inventoriesPage.getPage(),
                        inventoriesPage.getSize(),
                        inventoriesPage.getTotalPage(),
                        inventoriesPage.getIsLastPage()));
    }

    /**
     * 인벤토리 아이템 소비
     */
    @EntryLoggingPoint
    @PostMapping("/inventory/{mongId}")
    public ResponseEntity<ResponseDto<UseInventoryResponseDto>> useInventory(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId,
            @Valid @RequestBody UseInventoryRequestDto useInventoryRequestDto
    ) {
        UseInventoryCommand command = UseInventoryCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .inventoryId(useInventoryRequestDto.getInventoryId())
                .build();

        Mong mong = interactionUseCase.useInventoryUseCase(command);

        UseInventoryResponseDto useInventoryResponseDto = UseInventoryResponseDto.builder()
                .mongId(mong.getMongId())
                .payPoint(mong.getPayPoint())
                .expRatio(mong.getExp() / mong.getMaxStatus() * 100)
                .strengthRatio(mong.getStrength() / mong.getMaxStatus() * 100)
                .healthyRatio(mong.getHealthy() / mong.getMaxStatus() * 100)
                .satietyRatio(mong.getSatiety() / mong.getMaxStatus() * 100)
                .fatigueRatio(mong.getFatigue() / mong.getMaxStatus() * 100)
                .weight(mong.getWeight())
                .stateCode(mong.getStateCode())
                .statusCode(mong.getStatusCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.USE_INVENTORY.toResponseDto(useInventoryResponseDto));
    }

    /**
     * 랜덤 뽑기 티켓 구매
     */
    @EntryLoggingPoint
    @PostMapping("/randomDraw/ticket/{mongId}")
    public ResponseEntity<ResponseDto<BuyRandomDrawTicketResponseDto>> buyRandomDrawTicket(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        BuyRandomDrawTicketCommand command = BuyRandomDrawTicketCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        Mong mong = interactionUseCase.buyRandomDrawTicketUseCase(command);

        BuyRandomDrawTicketResponseDto buyRandomDrawTicketResponseDto = BuyRandomDrawTicketResponseDto.builder()
                .mongId(mong.getMongId())
                .payPoint(mong.getPayPoint())
                .randomDrawTicketCount(mong.getRandomDrawTicketCount())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.BUY_RANDOM_DRAW_TICKET.toResponseDto(buyRandomDrawTicketResponseDto));
    }

    /**
     * 랜덤 뽑기
     */
    @EntryLoggingPoint
    @PostMapping("/randomDraw/{mongId}")
    public ResponseEntity<ResponseDto<RandomDrawResponseDto>> randomDraw(
            @AuthenticationPrincipal Passport passport,
            @PathVariable("mongId") @NotNull @Min(1) Long mongId
    ) {
        RandomDrawCommand command = RandomDrawCommand.builder()
                .accountId(passport.getAccountId())
                .mongId(mongId)
                .build();

        RandomDraw randomDraw = interactionUseCase.randomDrawUseCase(command);

        RandomDrawResponseDto randomDrawResponseDto = RandomDrawResponseDto.builder()
                .randomDrawCode(randomDraw.getRandomDrawCode())
                .randomDrawName(randomDraw.getRandomDrawName())
                .inventoryTypeCode(randomDraw.getInventoryTypeCode())
                .build();

        return ResponseEntity.ok(AdapterInMongWebResponse.RANDOM_DRAW.toResponseDto(randomDrawResponseDto));
    }
}
