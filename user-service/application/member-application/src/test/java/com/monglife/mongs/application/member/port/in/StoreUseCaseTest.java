package com.monglife.mongs.application.member.port.in;

import com.monglife.core.utils.CommonUtil;
import com.monglife.mongs.application.member.port.exception.*;
import com.monglife.mongs.application.member.port.in.command.ConsumeOrderCommand;
import com.monglife.mongs.application.member.port.in.command.CreateOrderCommand;
import com.monglife.mongs.application.member.port.in.command.GetConsumedOrderCommand;
import com.monglife.mongs.application.member.port.in.service.StoreService;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import com.monglife.mongs.application.member.port.out.MemberPublishPort;
import com.monglife.mongs.application.member.port.out.OrderPersistencePort;
import com.monglife.mongs.domain.member.enums.OrderPurchaseTypeCode;
import com.monglife.mongs.domain.member.enums.OrderTypeCode;
import com.monglife.mongs.domain.member.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StoreUseCaseTest {

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final OrderPersistencePort orderPersistencePort = Mockito.mock(OrderPersistencePort.class);
    private final MemberPublishPort memberPublishPort = Mockito.mock(MemberPublishPort.class);
    private final GooglePaymentPort googlePaymentPort = Mockito.mock(GooglePaymentPort.class);
    private final StoreUseCase storeUseCase = new StoreService(memberPersistencePort, orderPersistencePort, memberPublishPort, googlePaymentPort);

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {
            // arrange
            final String productId = "PRDT000";
            final String productName = "TEST-PRODUCT-NAME";
            final Double price = 1000D;
            final String socialOrderId = "TEST-SOCIAL_ORDER-ID";
            final String purchaseToken = CommonUtil.randomId();
            final InAppProduct inAppProduct = InAppProduct.builder()
                    .productId(productId)
                    .productName(productName)
                    .price(price)
                    .build();
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(productId)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            Mockito.when(orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(ACCOUNT_ID, socialOrderId)).thenReturn(false);
            Mockito.when(googlePaymentPort.getInAppProductPort(Mockito.any())).thenReturn(Optional.of(inAppProduct));
            Mockito.when(orderPersistencePort.createOrderPort(Mockito.any())).thenReturn(Optional.of(order));

            // act & assert
            CreateOrderCommand command = CreateOrderCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .productId(productId)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            assertDoesNotThrow(() -> storeUseCase.createOrderUseCase(command));
            Mockito.verify(orderPersistencePort).createOrderPort(Mockito.any());
        }

        @Test
        @DisplayName("인앱 상품 정보가 없는 경우 주문을 등록하지 않고 예외가 발생 한다.")
        void notExistsInAppProduct() {
            // arrange
            final String productId = "PRDT000";
            final String socialOrderId = "TEST-SOCIAL_ORDER-ID";
            final String purchaseToken = CommonUtil.randomId();

            Mockito.when(orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(ACCOUNT_ID, socialOrderId)).thenReturn(false);
            Mockito.when(googlePaymentPort.getInAppProductPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateOrderCommand command = CreateOrderCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .productId(productId)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            assertThrows(NotExistsInAppProductException.class, () -> storeUseCase.createOrderUseCase(command));
            Mockito.verify(orderPersistencePort, Mockito.never()).createOrderPort(Mockito.any());
        }

        @Test
        @DisplayName("주문 등록에 실패하는 경우 예외가 발생 한다.")
        void createOrderFail() {
            // arrange
            final String productId = "PRDT000";
            final String productName = "TEST-PRODUCT-NAME";
            final Double price = 1000D;
            final String socialOrderId = "TEST-SOCIAL_ORDER-ID";
            final String purchaseToken = CommonUtil.randomId();
            final InAppProduct inAppProduct = InAppProduct.builder()
                    .productId(productId)
                    .productName(productName)
                    .price(price)
                    .build();

            Mockito.when(orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(ACCOUNT_ID, socialOrderId)).thenReturn(false);
            Mockito.when(googlePaymentPort.getInAppProductPort(Mockito.any())).thenReturn(Optional.of(inAppProduct));
            Mockito.when(orderPersistencePort.createOrderPort(Mockito.any())).thenReturn(Optional.empty());

            // act & assert
            CreateOrderCommand command = CreateOrderCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .productId(productId)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            assertThrows(InvalidCreateOrderException.class, () -> storeUseCase.createOrderUseCase(command));
        }
    }

    @Nested
    @DisplayName("주문 소비 단위 테스트")
    class ConsumeOrderUseCase {

        private static final Long ACCOUNT_ID = 1L;
        private static final String PRODUCT_ID = "PRDT000";
        private static final Double PRICE = 1000D;
        private static final String SOCIAL_ORDER_ID = CommonUtil.randomId();
        private static final String PURCHASE_TOKEN = CommonUtil.randomId();

        @Test
        @DisplayName("등록된 주문을 소비 처리 한다.")
        void consumeOrder() {
            // arrange
            final int starPoint = 100;
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            storeUseCase.consumeOrderUseCase(command);

            // assert
            assertEquals(starPoint, player.getStarPoint());
            assertEquals(OrderTypeCode.CONSUMED, inAppOrder.getOrderTypeCode());

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 주문이 없는 경우 예외가 발생 한다.")
        void notExistsOrder() {
            // arrange
            final int starPoint = 100;
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(NotExistsOrderException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort, Mockito.never()).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort, Mockito.never()).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 스타 포인트 환전 상품이 없는 경우 예외가 발생 한다.")
        void notExistsExchangeStarPointProduct() {
            // arrange
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(NotExistsExchangeStarPointProductException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort, Mockito.never()).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("등록된 플레이어가 없는 경우 예외가 발생 한다.")
        void notExistsPlayer() {
            // arrange
            final int starPoint = 100;
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort, Mockito.never()).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("플레이어를 수정할 때 플레이어가 없는 경우 예외가 발생 한다.")
        void notExistsPlayerWhenSavePlayer() {
            // arrange
            final int starPoint = 100;
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.empty());
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(NotExistsPlayerException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("인앱 상품 주문이 없는 경우 예외가 발생 한다.")
        void notExistsInAppOrder() {
            // arrange
            final int starPoint = 100;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(OrderPurchaseTypeCode.PAYED)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            final InAppOrder inAppOrderAfterConsume = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(OrderPurchaseTypeCode.PAYED)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.empty());
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.of(inAppOrderAfterConsume));

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(NotExistsInAppOrderException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort, Mockito.never()).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort, Mockito.never()).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }

        @Test
        @DisplayName("인앱 상품 주문을 소비할 수 없는 경우 예외가 발생한다.")
        void invalidConsumedInAppOrder() {
            // arrange
            final int starPoint = 100;
            final OrderPurchaseTypeCode orderPurchaseTypeCode = OrderPurchaseTypeCode.PAYED;
            final Order order = Order.builder()
                    .orderId(1L)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(PRICE)
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .build();

            final ExchangeStarPointProduct product = ExchangeStarPointProduct.builder()
                    .productId(PRODUCT_ID)
                    .starPoint(starPoint)
                    .build();

            final Player player = Player.builder()
                    .accountId(ACCOUNT_ID)
                    .slotCount(1)
                    .starPoint(0)
                    .build();

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .productId(PRODUCT_ID)
                    .purchaseToken(PURCHASE_TOKEN)
                    .orderPurchaseTypeCode(orderPurchaseTypeCode)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(orderPersistencePort.getExchangeStarPointProductPort(Mockito.any())).thenReturn(Optional.of(product));
            Mockito.when(memberPersistencePort.getPlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN)).thenReturn(Optional.of(inAppOrder));
            Mockito.when(memberPersistencePort.savePlayerPort(Mockito.any())).thenReturn(Optional.of(player));
            Mockito.when(googlePaymentPort.consumeInAppOrderPort(inAppOrder)).thenReturn(Optional.empty());

            // act & assert
            ConsumeOrderCommand command = ConsumeOrderCommand.builder()
                    .socialOrderId(SOCIAL_ORDER_ID)
                    .build();

            assertThrows(InvalidConsumeInAppOrderException.class, () -> storeUseCase.consumeOrderUseCase(command));

            Mockito.verify(orderPersistencePort).getOrderBySocialOrderIdPort(SOCIAL_ORDER_ID);
            Mockito.verify(orderPersistencePort).getExchangeStarPointProductPort(PRODUCT_ID);
            Mockito.verify(memberPersistencePort).getPlayerPort(ACCOUNT_ID);
            Mockito.verify(googlePaymentPort).getInAppOrderPort(PRODUCT_ID, SOCIAL_ORDER_ID, PURCHASE_TOKEN);
            Mockito.verify(memberPersistencePort).savePlayerPort(Mockito.any());
            Mockito.verify(googlePaymentPort).consumeInAppOrderPort(Mockito.any());
            Mockito.verify(memberPublishPort, Mockito.never()).publishStarPointPort(Mockito.any());
        }
    }

    @Nested
    @DisplayName("주문 소비 내역 목록 조회 단위 테스트")
    class GetConsumedOrderUseCase {

        private static final Long ACCOUNT_ID = 1L;

        @Test
        @DisplayName("소비된 주문 목록을 조회 한다.")
        void getConsumedOrder() {
            // arrange
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final Order order = Order.builder()
                            .orderId(1L)
                            .accountId(ACCOUNT_ID)
                            .productId("PRDT000")
                            .price(1000D)
                            .socialOrderId(socialOrderId)
                            .purchaseToken(purchaseToken)
                            .build();
            final List<Order> orders = List.of(order);

            final InAppOrder inAppOrder = InAppOrder.builder()
                    .socialOrderId(socialOrderId)
                    .productId(order.getProductId())
                    .purchaseToken(purchaseToken)
                    .orderPurchaseTypeCode(OrderPurchaseTypeCode.PAYED)
                    .orderTypeCode(OrderTypeCode.CONSUMED)
                    .purchasedAt(LocalDateTime.now())
                    .build();

            Mockito.when(orderPersistencePort.getOrderBySocialOrderIdPort(Mockito.any())).thenReturn(Optional.of(order));
            Mockito.when(googlePaymentPort.getInAppOrderPort(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Optional.of(inAppOrder));

            // act
            GetConsumedOrderCommand command = GetConsumedOrderCommand.builder()
                    .socialOrderIds(List.of(socialOrderId))
                    .build();

            List<Order> expected = storeUseCase.getConsumedOrderUseCase(command);

            // assert
            assertEquals(expected, orders);
        }
    }
}