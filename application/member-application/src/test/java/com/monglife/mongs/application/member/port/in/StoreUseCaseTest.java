package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.exception.*;
import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.application.member.port.in.service.StoreService;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.domain.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.enums.OrderTypeCode;
import com.monglife.mongs.domain.exception.AlreadyConsumedInAppOrderException;
import com.monglife.mongs.domain.exception.AlreadyConsumedOrderException;
import com.monglife.mongs.domain.exception.PaymentNotCompletedInAppOrderException;
import com.monglife.mongs.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private static final Long accountId = 1L;

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderUseCase {

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {
            // arrange
            String productId = "PRDT000";
            String productName = "TEST-PRODUCT-NAME";
            Double price = 1000D;
            String socialOrderId = "TEST-SOCIAL_ORDER-ID";
            String purchaseToken = UUID.randomUUID().toString().replace("-", "");
            InAppProduct inAppProduct = InAppProduct.builder()
                    .productId(productId)
                    .productName(productName)
                    .price(price)
                    .build();

            Mockito.when(googlePaymentPort.getInAppProductPort(Mockito.any())).thenReturn(Optional.of(inAppProduct));

            // act & assert
            CreateOrderCommand command = CreateOrderCommand.builder()
                    .accountId(accountId)
                    .productId(productId)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            assertDoesNotThrow(() -> storeService.createOrderUseCase(command));
            Mockito.verify(memberPersistencePort).createOrderPort(Mockito.any());
        }

        @Test
        @DisplayName("인앱 상품 정보가 없는 경우 주문을 등록하지 않고 예외가 발생 한다.")
        void notExistsInAppProduct() {
            // arrange
            String productId = "PRDT000";
            String socialOrderId = "TEST-SOCIAL_ORDER-ID";
            String purchaseToken = UUID.randomUUID().toString().replace("-", "");

            Mockito.when(googlePaymentPort.getInAppProductPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateOrderCommand command = CreateOrderCommand.builder()
                    .accountId(accountId)
                    .productId(productId)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            assertThrows(NotExistsInAppProductException.class, () -> storeService.createOrderUseCase(command));
            Mockito.verify(memberPersistencePort, Mockito.never()).createOrderPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("주문 소비 단위 테스트")
    class ConsumeOrderUseCase {

        private final String productId = "PRDT000";
        private final Double price = 1000D;
        private final String socialOrderId = UUID.randomUUID().toString().replace("-", "");
        private final String purchaseToken = UUID.randomUUID().toString().replace("-", "");

        @Test
        @DisplayName("등록된 주문을 소비 처리 한다.")
        void consumeOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            storeService.consumeOrderUseCase(command);

            // assert
            assertEquals(starPoint, player.getStarPoint());
            assertEquals(OrderTypeCode.CONSUMED, inAppOrder.getOrderTypeCode());
            assertEquals(OrderTypeCode.CONSUMED, order.getOrderTypeCode());

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 주문이 없는 경우 예외가 발생 한다.")
        void notExistsOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort, Mockito.never()).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort, Mockito.never()).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("주문을 수정할 때 주문이 없는 경우 예외가 발생 한다.")
        void notExistsOrderWhenSaveOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 스타 포인트 환전 상품이 없는 경우 예외가 발생 한다.")
        void notExistsExchangeStarPointProduct() {
            // arrange
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsExchangeStarPointProductException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort, Mockito.never()).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 플레이어가 없는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 없는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("인앱 상품 주문이 없는 경우 예외가 발생 한다.")
        void notExistsInAppOrder() {
            // arrange
            int starPoint = 100;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(NotExistsInAppOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("이미 소비된 주문인 경우 예외가 발생한다.")
        void alreadyConsumedOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(AlreadyConsumedOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort, Mockito.never()).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());

        }

        @Test
        @DisplayName("구매가 완료되지 않은 주문인 경우 예외가 발생한다.")
        void notPayedInAppOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PENDING;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(PaymentNotCompletedInAppOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("구매가 취소된 주문인 경우 예외가 발생한다.")
        void payCancelInAppOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.CANCEL;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(PaymentNotCompletedInAppOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("이미 소비된 인앱 상품 주문인 경우 예외가 발생한다.")
        void alreadyConsumedInAppOrder() {
            // arrange
            int starPoint = 100;
            OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            Order order = Order.builder()
                    .orderId(1L)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(productId)
                    .starPoint(starPoint)
                    .build();

            Player player = Player.builder()
                    .accountId(accountId)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(productId)
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(memberPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(memberPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(productId, socialOrderId, purchaseToken)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(memberPersistencePort.saveOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(socialOrderId)
                    .build();

            assertThrows(AlreadyConsumedInAppOrderException.class, () -> storeService.consumeOrderUseCase(command));

            Mockito.verify(memberPersistencePort).getOrderBySocialOrderIdPort(socialOrderId);
            Mockito.verify(memberPersistencePort).getExchangeStarPointProductPort(productId);
            Mockito.verify(memberPersistencePort).getPlayerPort(accountId);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(productId, socialOrderId, purchaseToken);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(memberPersistencePort).saveOrderPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("인앱 상품 목록 조회 단위 테스트")
    class GetProductsUseCase {

        @Test
        @DisplayName("인앱 상품 목록을 조회 한다.")
        void getProducts() {
            // arrange
            List<String> productIds = List.of("PRDT000", "PRDT001", "PRDT002");
            List<InAppProduct> inAppProducts = List.of(
                    InAppProduct.builder()
                            .productId(productIds.get(0))
                            .productName("[1] TEST-PRODUCT-NAME")
                            .price(1000D)
                            .build(),
                    InAppProduct.builder()
                            .productId(productIds.get(1))
                            .productName("[2] TEST-PRODUCT-NAME")
                            .price(2000D)
                            .build(),
                    InAppProduct.builder()
                            .productId(productIds.get(2))
                            .productName("[3] TEST-PRODUCT-NAME")
                            .price(3000D)
                            .build());

            Mockito.when(memberPersistencePort.getProductIdsPort()).thenReturn(productIds);
            Mockito.when(googlePaymentPort.getInAppProductsPort(productIds)).thenReturn(inAppProducts);

            // act
            List<InAppProduct> expected = storeService.getProductsUseCase();

            // assert
            assertEquals(expected, inAppProducts);
            Mockito.verify(memberPersistencePort).getProductIdsPort();
            Mockito.verify(googlePaymentPort).getInAppProductsPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("주문 소비 내역 목록 조회 단위 테스트")
    class GetConsumedOrderUseCase {

        @Test
        @DisplayName("소비된 주문 목록을 조회 한다.")
        void getConsumedOrder() {
            // arrange
            List<Order> orders = List.of(
                    Order.builder()
                            .orderId(1L)
                            .accountId(accountId)
                            .productId("PRDT000")
                            .orderTypeCode(OrderTypeCode.CONSUMED)
                            .price(1000D)
                            .socialOrderId(UUID.randomUUID().toString().replace("-", ""))
                            .purchaseToken(UUID.randomUUID().toString().replace("-", ""))
                            .build(),
                    Order.builder()
                            .orderId(2L)
                            .accountId(accountId)
                            .productId("PRDT001")
                            .orderTypeCode(OrderTypeCode.CONSUMED)
                            .price(2000D)
                            .socialOrderId(UUID.randomUUID().toString().replace("-", ""))
                            .purchaseToken(UUID.randomUUID().toString().replace("-", ""))
                            .build(),
                    Order.builder()
                            .orderId(1L)
                            .accountId(accountId)
                            .productId("PRDT002")
                            .orderTypeCode(OrderTypeCode.CONSUMED)
                            .price(3000D)
                            .socialOrderId(UUID.randomUUID().toString().replace("-", ""))
                            .purchaseToken(UUID.randomUUID().toString().replace("-", ""))
                            .build());

            Mockito.when(memberPersistencePort.getConsumedOrdersPort(Mockito.any())).thenReturn(orders);

            // act
            GetConsumedOrderCommand command = GetConsumedOrderCommand.builder()
                    .accountId(accountId)
                    .build();

            List<Order> expected = storeService.getConsumedOrderUseCase(command);

            // assert
            assertEquals(expected, orders);
            Mockito.verify(memberPersistencePort).getConsumedOrdersPort(Mockito.any());
        }
    }
}