package com.monglife.mongs.app.user.store.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.mongs.app.user.store.dto.etc.GetProductDto;
import com.monglife.mongs.app.user.store.dto.request.ConsumeProductOrderRequestDto;
import com.monglife.mongs.app.user.store.dto.response.GetProductResponseDto;
import com.monglife.mongs.app.user.store.enums.StoreResponse;
import com.monglife.mongs.app.user.store.service.StoreService;
import com.monglife.mongs.module.security.global.principal.Passport;
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

    private final StoreService storeService;


    @GetMapping("/product")
    public ResponseEntity<ResponseDto<List<GetProductResponseDto>>> getProducts() {

        List<GetProductDto> getInAppProductDtos = storeService.getProducts();

        List<GetProductResponseDto> getProductResponseDtos = getInAppProductDtos.stream()
                .map(getInAppProductDto -> GetProductResponseDto.builder()
                        .productId(getInAppProductDto.getProductId())
                        .build())
                .toList();

        return ResponseEntity.ok(StoreResponse.APP_USER_STORE_GET_PRODUCT.toResponseDto(getProductResponseDtos));
    }

    @PutMapping("/order")
    public ResponseEntity<ResponseDto<?>> consumeProductOrder(@AuthenticationPrincipal Passport passport, @RequestBody ConsumeProductOrderRequestDto consumeProductOrderRequestDto) {

        Long accountId = passport.getAccountId();
        String productId = consumeProductOrderRequestDto.getProductId();
        String purchaseToken = consumeProductOrderRequestDto.getPurchaseToken();

        Long productOrderId = storeService.createProductOrder(accountId, productId, purchaseToken);

        storeService.consumeProductOrder(productOrderId, purchaseToken);

        return ResponseEntity.ok(StoreResponse.APP_USER_STORE_CONSUME_ORDER.toResponseDto());
    }
}
