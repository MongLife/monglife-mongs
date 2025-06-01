package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.core.utils.CommonUtil;
import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.module.common.jpa.entity.ComnCodeEntity;
import com.monglife.module.common.jpa.entity.GroupCodeEntity;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.entity.ExchangeStarPointProductEntity;
import com.monglife.mongs.adapter.out.member.persistence.entity.OrderEntity;
import com.monglife.mongs.adapter.out.member.persistence.repository.ComnCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.ExchangeStarPointProductRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.GroupCodeRepository;
import com.monglife.mongs.adapter.out.member.persistence.repository.OrderRepository;
import com.monglife.mongs.application.member.port.out.OrderPersistencePort;
import com.monglife.mongs.application.member.port.out.vo.CreateOrderVo;
import com.monglife.mongs.domain.member.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        AdapterOutMemberPersistenceConfig.class,
        MemberDataSourceConfig.class,
        HibernateAutoConfig.class
})
class OrderPersistenceServiceTest {

    private final OrderPersistencePort orderPersistencePort;
    private final GroupCodeRepository groupCodeRepository;
    private final ComnCodeRepository comnCodeRepository;
    private final OrderRepository orderRepository;
    private final ExchangeStarPointProductRepository exchangeStarPointProductRepository;

    @Autowired
    public OrderPersistenceServiceTest(OrderPersistencePort orderPersistencePort, GroupCodeRepository groupCodeRepository, ComnCodeRepository comnCodeRepository, OrderRepository orderRepository, ExchangeStarPointProductRepository exchangeStarPointProductRepository) {
        this.orderPersistencePort = orderPersistencePort;
        this.groupCodeRepository = groupCodeRepository;
        this.comnCodeRepository = comnCodeRepository;
        this.orderRepository = orderRepository;
        this.exchangeStarPointProductRepository = exchangeStarPointProductRepository;
    }

    private static final Long ACCOUNT_ID = 1L;
    private static final String PRODUCT_ID = "PRDT000";
    private static final String PRODUCT_NAME = "테스트 상품명";
    private static final GroupCodeEntity PRODUCT_GROUP_CODE_ENTITY = new GroupCodeEntity("PRDT", "인앱 상품 그룹 코드");
    private static final ComnCodeEntity PRODUCT_TYPE = new ComnCodeEntity(PRODUCT_ID, PRODUCT_NAME, PRODUCT_GROUP_CODE_ENTITY);

    @BeforeEach
    void beforeEach() {
        groupCodeRepository.saveAndFlush(PRODUCT_GROUP_CODE_ENTITY);
        comnCodeRepository.saveAndFlush(PRODUCT_TYPE);
    }

    @Nested
    @DisplayName("주문 존재 여부 확인 단위 테스트")
    class IsExistsOrderBySocialOrderIdPort {

        @Test
        @DisplayName("주문이 존재하는 경우 true를 반환 한다.")
        void isExistOrderBySocialOrderId() {
            // arrange
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();

            orderRepository.saveAndFlush(OrderEntity.builder()
                            .accountId(ACCOUNT_ID)
                            .productType(PRODUCT_TYPE)
                            .price(0D)
                            .socialOrderId(socialOrderId)
                            .purchaseToken(purchaseToken)
                            .build());

            // act
            var expected = orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(ACCOUNT_ID, socialOrderId);

            // assert
            assertTrue(expected);
        }

        @Test
        @DisplayName("주문이 존재하지 않는 경우 false를 반환 한다.")
        void notExistOrderBySocialOrderId() {
            // arrange
            final String socialOrderId = CommonUtil.randomId();

            // act
            var expected = orderPersistencePort.isExistsOrderByAccountIdAndSocialOrderIdPort(ACCOUNT_ID, socialOrderId);

            // assert
            assertFalse(expected);
        }
    }

    @Nested
    @DisplayName("스타 포인트 환전 상품 조회 단위 테스트")
    class GetExchangeStarPointProductPort {

        @Test
        @DisplayName("스타 포인트 환전 상품을 조회 한다.")
        void getExchangeStarPointProduct() {
            // arrange
            final int starPoint = 100;

            exchangeStarPointProductRepository.saveAndFlush(ExchangeStarPointProductEntity.builder()
                    .productId(PRODUCT_ID)
                    .productName(PRODUCT_NAME)
                    .starPoint(starPoint)
                    .build());

            // act
            var expected = orderPersistencePort.getExchangeStarPointProductPort(PRODUCT_ID);

            // assert
            assertTrue(expected.isPresent());
            assertEquals(PRODUCT_ID, expected.get().getProductId());
            assertEquals(starPoint, expected.get().getStarPoint());
        }

        @Test
        @DisplayName("스타 포인트 환전 상품이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getExchangeStarPointProductWhenNotExistProduct() {
            // act
            var expected = orderPersistencePort.getExchangeStarPointProductPort(PRODUCT_ID);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderPort {

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {
            // arrange
            final double price = 1000D;
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final CreateOrderVo createOrderVo = CreateOrderVo.builder()
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            var expected = orderPersistencePort.createOrderPort(createOrderVo);

            // assert
            assertTrue(expected.isPresent());
            assertEquals(ACCOUNT_ID, expected.get().getAccountId());
            assertEquals(PRODUCT_ID, expected.get().getProductId());
            assertEquals(price, expected.get().getPrice());
            assertEquals(socialOrderId, expected.get().getSocialOrderId());
            assertEquals(purchaseToken, expected.get().getPurchaseToken());
        }

        @Test
        @DisplayName("상품 공통 코드가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void createOrderWhenNotExistProduct() {
            // arrange
            final String productId = "PRDT999";
            final double price = 1000D;
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final CreateOrderVo createOrderVo = CreateOrderVo.builder()
                    .accountId(ACCOUNT_ID)
                    .productId(productId)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            var expected = orderPersistencePort.createOrderPort(createOrderVo);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("주문 수정 단위 테스트")
    class SaveOrderPort {

        @Test
        @DisplayName("주문을 수정 한다.")
        void saveOrder() {
            // arrange
            final double price = 1000D;
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final long orderId = orderRepository.saveAndFlush(OrderEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .productType(PRODUCT_TYPE)
                    .price(0D)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build())
                    .getOrderId();
            final Order order = Order.builder()
                    .orderId(orderId)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            var expected = orderPersistencePort.saveOrderPort(order);

            // assert
            assertTrue(expected.isPresent());
            assertEquals(orderId, expected.get().getOrderId());
            assertEquals(ACCOUNT_ID, expected.get().getAccountId());
            assertEquals(PRODUCT_ID, expected.get().getProductId());
            assertEquals(price, expected.get().getPrice());
            assertEquals(socialOrderId, expected.get().getSocialOrderId());
            assertEquals(purchaseToken, expected.get().getPurchaseToken());
        }

        @Test
        @DisplayName("주문이 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void saveOrderWhenNotExistsOrder() {
            // arrange
            final long orderId = 1L;
            final double price = 1000D;
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final Order order = Order.builder()
                    .orderId(orderId)
                    .accountId(ACCOUNT_ID)
                    .productId(PRODUCT_ID)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            var expected = orderPersistencePort.saveOrderPort(order);

            // assert
            assertTrue(expected.isEmpty());
        }
    }

    @Nested
    @DisplayName("인앱 주문 ID 기준 주문 조회 단위 테스트")
    class GetOrderBySocialOrderIdPort {

        @Test
        @DisplayName("인앱 주문 ID 기준으로 주문을 조회 한다.")
        void getOrderBySocialOrderId() {
            // arrange
            final double price = 1000D;
            final String socialOrderId = CommonUtil.randomId();
            final String purchaseToken = CommonUtil.randomId();
            final long orderId = orderRepository.saveAndFlush(OrderEntity.builder()
                    .accountId(ACCOUNT_ID)
                    .productType(PRODUCT_TYPE)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build())
                    .getOrderId();

            // act
            var expected = orderPersistencePort.getOrderBySocialOrderIdPort(socialOrderId);

            // assert
            assertTrue(expected.isPresent());
            assertEquals(orderId, expected.get().getOrderId());
            assertEquals(ACCOUNT_ID, expected.get().getAccountId());
            assertEquals(PRODUCT_ID, expected.get().getProductId());
            assertEquals(price, expected.get().getPrice());
            assertEquals(socialOrderId, expected.get().getSocialOrderId());
            assertEquals(purchaseToken, expected.get().getPurchaseToken());
        }

        @Test
        @DisplayName("주문 내역이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getOrderBySocialOrderIdWhenNotExistsOrder() {
            // arrange
            final String socialOrderId = CommonUtil.randomId();

            // act
            var expected = orderPersistencePort.getOrderBySocialOrderIdPort(socialOrderId);

            // assert
            assertTrue(expected.isEmpty());
        }
    }
}