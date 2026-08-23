package com.monglife.mongs.adapter.in.member.web.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.role.RoleCode;
import com.monglife.module.common.logging.annotation.EntryLoggingPoint;
import com.monglife.module.common.security.annotation.AuthCheck;
import com.monglife.module.common.security.principal.Passport;
import com.monglife.mongs.adapter.in.member.web.enums.AdapterInMemberWebResponse;
import com.monglife.mongs.adapter.in.member.web.dto.request.ConsumeOrderRequestDto;
import com.monglife.mongs.adapter.in.member.web.dto.request.GetConsumedOrdersRequestDto;
import com.monglife.mongs.adapter.in.member.web.dto.response.ConsumeOrderResponseDto;
import com.monglife.mongs.adapter.in.member.web.dto.response.GetConsumedOrderResponseDto;
import com.monglife.mongs.adapter.in.member.web.dto.response.GetProductResponseDto;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.in.StoreUseCase;
import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetPlayerCommand;
import com.monglife.mongs.domain.member.model.InAppProduct;
import com.monglife.mongs.domain.member.model.Order;
import com.monglife.mongs.domain.member.model.Player;
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
    @AuthCheck({ RoleCode.NORMAL, RoleCode.SUBSCRIBER, RoleCode.ADMIN })
    @EntryLoggingPoint
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

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_IN_APP_PRODUCTS.toResponseDto(getProductResponseDtos));
    }

    /**
     * 소비된 주문 목록 조회
     */
    @AuthCheck({ RoleCode.NORMAL, RoleCode.SUBSCRIBER, RoleCode.ADMIN })
    @EntryLoggingPoint
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

        return ResponseEntity.ok(AdapterInMemberWebResponse.GET_CONSUMED_ORDERS.toResponseDto(getConsumedOrderResponseDtos));
    }

    /**
     * 주문 소비
     */
    @AuthCheck({ RoleCode.NORMAL, RoleCode.SUBSCRIBER, RoleCode.ADMIN })
    @EntryLoggingPoint
    @PostMapping("/order/consume")
    public ResponseEntity<ResponseDto<ConsumeOrderResponseDto>> consumeOrder(
            @AuthenticationPrincipal Passport passport,
            @Valid @RequestBody ConsumeOrderRequestDto consumeOrderRequestDto
    ) {
        /**
         * productId 는 여기서 대문자로 정규화한다.
         *
         * Google Play 의 상품 ID 는 소문자(prdt000)이고 우리 규약은 대문자(PRDT000)다.
         * 클라이언트가 Play 가 준 값을 그대로 보내면 OrderPersistenceService 의
         * comnCodeRepository.findById 가 monglife_comn_code 에서 빗나가 주문 생성이
         * 실패하고, 그 결과 Google 소비까지 도달하지 못해 구매가 계정에 보유 상태로
         * 남는다(이후 모든 구매가 ITEM_ALREADY_OWNED 로 거부됨).
         *
         * 클라이언트도 대문자로 보내도록 고쳤지만 구버전 앱이 남아 있으므로
         * 외부 입력이 들어오는 이 경계에서 한 번 더 막는다.
         */
        String productId = consumeOrderRequestDto.getProductId().toUpperCase();

        // 새로운 주문 생성
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .accountId(passport.getAccountId())
                .productId(productId)
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

        return ResponseEntity.ok(AdapterInMemberWebResponse.CONSUME_ORDER.toResponseDto(consumeOrderResponseDto));
    }
}
