package com.monglife.mongs.adapter.in.member.web.store.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInWebMemberResponse;
import com.monglife.mongs.adapter.in.member.web.store.dto.request.ConsumeOrderRequestDto;
import com.monglife.mongs.adapter.in.member.web.store.dto.request.GetConsumedOrdersRequestDto;
import com.monglife.mongs.adapter.in.member.web.store.dto.response.ConsumeOrderResponseDto;
import com.monglife.mongs.adapter.in.member.web.store.dto.response.GetConsumedOrderResponseDto;
import com.monglife.mongs.adapter.in.member.web.store.dto.response.GetProductResponseDto;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.StoreUseCase;
import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetPlayerCommand;
import com.monglife.mongs.domain.model.InAppProduct;
import com.monglife.mongs.domain.model.Order;
import com.monglife.mongs.domain.model.Player;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreUseCase storeUseCase;

    private final PlayerUseCase playerUseCase;

    /**
     * 인앱 상품 목록 조회
     */
    @GetMapping("/product")
    public ResponseEntity<ResponseDto<List<GetProductResponseDto>>> getProducts() {

        List<InAppProduct> inAppProducts = storeUseCase.getProductsUseCase();

        List<GetProductResponseDto> getProductResponseDtos = inAppProducts.stream()
                .map(inAppProduct -> GetProductResponseDto.builder()
                        .productId(inAppProduct.getProductId())
                        .productName(inAppProduct.getProductName())
                        .price(inAppProduct.getPrice())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInWebMemberResponse.GET_IN_APP_PRODUCTS.toResponseDto(getProductResponseDtos));
    }

    /**
     * 소비된 주문 목록 조회
     */
    @PostMapping("/order")
    public ResponseEntity<ResponseDto<List<GetConsumedOrderResponseDto>>> getConsumedOrders(
            @Valid @RequestBody GetConsumedOrdersRequestDto getConsumedOrdersRequestDto
    ) {

        GetConsumedOrderCommand command = GetConsumedOrderCommand.builder()
                .socialOrderIds(getConsumedOrdersRequestDto.getSocialOrderIds())
                .build();

        List<Order> orders = storeUseCase.getConsumedOrderUseCase(command);

        List<GetConsumedOrderResponseDto> getConsumedOrderResponseDtos = orders.stream()
                .map(order -> GetConsumedOrderResponseDto.builder()
                        .orderId(order.getOrderId())
                        .socialOrderId(order.getSocialOrderId())
                        .productId(order.getProductId())
                        .build())
                .toList();

        return ResponseEntity.ok(AdapterInWebMemberResponse.GET_CONSUMED_ORDERS.toResponseDto(getConsumedOrderResponseDtos));
    }

    /**
     * 주문 소비
     */
    @PostMapping("/order/consume")
    public ResponseEntity<ResponseDto<ConsumeOrderResponseDto>> consumeOrder(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody ConsumeOrderRequestDto consumeOrderRequestDto
    ) {
        // 새로운 주문 생성
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .accountId(passport.getAccountId())
                .productId(consumeOrderRequestDto.getProductId())
                .socialOrderId(consumeOrderRequestDto.getSocialOrderId())
                .purchaseToken(consumeOrderRequestDto.getPurchaseToken())
                .build();

        storeUseCase.createOrderUseCase(createOrderCommand);

        // 주문 소비
        ConsumeOrderCommand consumeOrderCommand = ConsumeOrderCommand.builder()
                .socialOrderId(consumeOrderRequestDto.getSocialOrderId())
                .build();

        Order order = storeUseCase.consumeOrderUseCase(consumeOrderCommand);

        // 플레이어 조회
        GetPlayerCommand getPlayerCommand = GetPlayerCommand.builder()
                .accountId(order.getAccountId())
                .build();

        Player player = playerUseCase.getPlayerUseCase(getPlayerCommand);

        ConsumeOrderResponseDto consumeOrderResponseDto = ConsumeOrderResponseDto.builder()
                .accountId(player.getAccountId())
                .orderId(order.getOrderId())
                .socialOrderId(order.getSocialOrderId())
                .productId(order.getProductId())
                .purchaseToken(order.getPurchaseToken())
                .starPoint(player.getStarPoint())
                .slotCount(player.getSlotCount())
                .build();

        return ResponseEntity.ok(AdapterInWebMemberResponse.CONSUME_ORDER.toResponseDto(consumeOrderResponseDto));
    }
}
