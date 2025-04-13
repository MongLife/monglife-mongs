package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.service.StoreService;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StoreUseCaseTest {

    private final MemberPersistencePort memberPersistencePort;

    private final MemberPublishPort memberPublishPort;

    private final GooglePaymentPort googlePaymentPort;

    private final StoreService storeService;

    public StoreUseCaseTest() {
        this.memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
        this.memberPublishPort = Mockito.mock(MemberPublishPort.class);
        this.googlePaymentPort = Mockito.mock(GooglePaymentPort.class);
        this.storeService = new StoreService(memberPersistencePort, memberPublishPort, googlePaymentPort);
    }

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderUseCase {

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("주문 소비 단위 테스트")
    class ConsumeOrderUseCase {

        @Test
        @DisplayName("등록된 주문을 소비 처리 한다.")
        void consumeOrder() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("인앱 상품 목록 조회 단위 테스트")
    class GetProductsUseCase {

        @Test
        @DisplayName("인앱 상품 목록을 조회 한다.")
        void getProducts() {
            // arrange

            // act

            // assert

        }
    }

    @Nested
    @DisplayName("주문 소비 내역 목록 조회 단위 테스트")
    class GetConsumedOrderUseCase {

        @Test
        @DisplayName("소비된 주문 목록을 조회 한다.")
        void getConsumedOrder() {
            // arrange

            // act

            // assert

        }
    }
}