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
import com.monglife.mongs.domain.enums.OrderTypeCode;
import com.monglife.mongs.domain.model.ExchangeStarPointProduct;
import com.monglife.mongs.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutMemberPersistenceConfig.class, MemberDataSourceConfig.class, HibernateAutoConfig.class })
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

    private static final Long accountId = 1L;
    private static final String productId = "PRDT000";
    private static final String productName = "테스트 상품명";
    private static final GroupCodeEntity productGroupCodeEntity = new GroupCodeEntity("PRDT", "인앱 상품 그룹 코드");
    private static final ComnCodeEntity productType = new ComnCodeEntity(productId, productName, productGroupCodeEntity);

    @BeforeEach
    void beforeEach() {
        groupCodeRepository.saveAndFlush(productGroupCodeEntity);
        comnCodeRepository.saveAndFlush(productType);
    }

    @Nested
    @DisplayName("스타 포인트 환전 상품 조회 단위 테스트")
    class GetExchangeStarPointProductPort {

        @Test
        @DisplayName("스타 포인트 환전 상품을 조회 한다.")
        void getExchangeStarPointProduct() {
            // arrange
            int starPoint = 100;

            exchangeStarPointProductRepository.saveAndFlush(ExchangeStarPointProductEntity.builder()
                    .productId(productId)
                    .productName(productName)
                    .starPoint(starPoint)
                    .build());

            // act
            Optional<ExchangeStarPointProduct> exchangeStarPointProductOptional = orderPersistencePort.getExchangeStarPointProductPort(productId);

            // assert
            assertTrue(exchangeStarPointProductOptional.isPresent());
            assertEquals(productId, exchangeStarPointProductOptional.get().getProductId());
            assertEquals(starPoint, exchangeStarPointProductOptional.get().getStarPoint());
        }

        @Test
        @DisplayName("스타 포인트 환전 상품이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getExchangeStarPointProductWhenNotExistProduct() {
            // act
            Optional<ExchangeStarPointProduct> exchangeStarPointProductOptional = orderPersistencePort.getExchangeStarPointProductPort(productId);

            // assert
            assertTrue(exchangeStarPointProductOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderPort {

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {
            // arrange
            double price = 1000D;
            String socialOrderId = CommonUtil.randomId();
            String purchaseToken = CommonUtil.randomId();
            CreateOrderVo createOrderVo = CreateOrderVo.builder()
                    .accountId(accountId)
                    .productId(productId)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            Optional<Order> orderOptional = orderPersistencePort.createOrderPort(createOrderVo);

            // assert
            assertTrue(orderOptional.isPresent());
            assertEquals(accountId, orderOptional.get().getAccountId());
            assertEquals(productId, orderOptional.get().getProductId());
            assertEquals(OrderTypeCode.ORDERED, orderOptional.get().getOrderTypeCode());
            assertEquals(price, orderOptional.get().getPrice());
            assertEquals(socialOrderId, orderOptional.get().getSocialOrderId());
            assertEquals(purchaseToken, orderOptional.get().getPurchaseToken());
        }

        @Test
        @DisplayName("상품 공통 코드가 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void createOrderWhenNotExistProduct() {
            // arrange
            String productId = "PRDT999";
            double price = 1000D;
            String socialOrderId = CommonUtil.randomId();
            String purchaseToken = CommonUtil.randomId();
            CreateOrderVo createOrderVo = CreateOrderVo.builder()
                    .accountId(accountId)
                    .productId(productId)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            Optional<Order> orderOptional = orderPersistencePort.createOrderPort(createOrderVo);

            // assert
            assertTrue(orderOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("주문 수정 단위 테스트")
    class SaveOrderPort {

        @Test
        @DisplayName("주문을 수정 한다.")
        void saveOrder() {
            // arrange
            double price = 1000D;
            String socialOrderId = CommonUtil.randomId();
            String purchaseToken = CommonUtil.randomId();

            long orderId = orderRepository.saveAndFlush(OrderEntity.builder()
                    .accountId(accountId)
                    .productType(productType)
                    .price(0D)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .isConsumed(false)
                    .build())
                    .getOrderId();

            Order order = Order.builder()
                    .orderId(orderId)
                    .accountId(accountId)
                    .productId(productId)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .build();

            // act
            Optional<Order> orderOptional = orderPersistencePort.saveOrderPort(order);

            // assert
            assertTrue(orderOptional.isPresent());
            assertEquals(orderId, orderOptional.get().getOrderId());
            assertEquals(accountId, orderOptional.get().getAccountId());
            assertEquals(productId, orderOptional.get().getProductId());
            assertEquals(OrderTypeCode.ORDERED, orderOptional.get().getOrderTypeCode());
            assertEquals(price, orderOptional.get().getPrice());
            assertEquals(socialOrderId, orderOptional.get().getSocialOrderId());
            assertEquals(purchaseToken, orderOptional.get().getPurchaseToken());
        }

        @Test
        @DisplayName("주문이 존재하지 않는 경우 빈 옵셔널 객체를 반환 한다.")
        void saveOrderWhenNotExistsOrder() {
            // arrange
            long orderId = 1L;
            double price = 1000D;
            String socialOrderId = CommonUtil.randomId();
            String purchaseToken = CommonUtil.randomId();

            Order order = Order.builder()
                    .orderId(orderId)
                    .accountId(accountId)
                    .productId(productId)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .orderTypeCode(OrderTypeCode.ORDERED)
                    .build();

            // act
            Optional<Order> orderOptional = orderPersistencePort.saveOrderPort(order);

            // assert
            assertTrue(orderOptional.isEmpty());
        }
    }

    @Nested
    @DisplayName("소비된 주문 목록 조회 단위 테스트")
    class GetConsumedOrdersPort {

        @Test
        @DisplayName("소비된 주문 목록을 조회 한다.")
        void getConsumedOrders() {
            // arrange
            int orderCount = 10;
            int consumedOrderCount = 5;
            List<OrderEntity> orderEntities = new ArrayList<>();

            for (long index = 1L; index <= orderCount; index++) {
                OrderEntity orderEntity = OrderEntity.builder()
                        .accountId(accountId)
                        .productType(productType)
                        .price(0D)
                        .socialOrderId(CommonUtil.randomId())
                        .purchaseToken(CommonUtil.randomId())
                        .isConsumed(index <= consumedOrderCount)
                        .build();

                orderEntities.add(orderEntity);
            }

            orderRepository.saveAllAndFlush(orderEntities);

            // act
            List<Order> orders = orderPersistencePort.getConsumedOrdersPort(accountId);

            // assert
            for (long index = 1L; index <= consumedOrderCount; index++) {
                Order order = orders.get((int) index - 1);
                assertEquals(accountId, order.getAccountId());
                assertEquals(productId, order.getProductId());
                assertEquals(OrderTypeCode.CONSUMED, order.getOrderTypeCode());
            }
        }
    }

    @Nested
    @DisplayName("인앱 주문 ID 기준 주문 조회 단위 테스트")
    class GetOrderBySocialOrderIdPort {

        @Test
        @DisplayName("인앱 주문 ID 기준으로 주문을 조회 한다.")
        void getOrderBySocialOrderId() {
            // arrange
            double price = 1000D;
            String socialOrderId = CommonUtil.randomId();
            String purchaseToken = CommonUtil.randomId();

            long orderId = orderRepository.saveAndFlush(OrderEntity.builder()
                    .accountId(accountId)
                    .productType(productType)
                    .price(price)
                    .socialOrderId(socialOrderId)
                    .purchaseToken(purchaseToken)
                    .isConsumed(false)
                    .build())
                    .getOrderId();

            // act
            Optional<Order> orderOptional = orderPersistencePort.getOrderBySocialOrderIdPort(socialOrderId);

            // assert
            assertTrue(orderOptional.isPresent());
            assertEquals(orderId, orderOptional.get().getOrderId());
            assertEquals(accountId, orderOptional.get().getAccountId());
            assertEquals(productId, orderOptional.get().getProductId());
            assertEquals(price, orderOptional.get().getPrice());
            assertEquals(OrderTypeCode.ORDERED, orderOptional.get().getOrderTypeCode());
            assertEquals(socialOrderId, orderOptional.get().getSocialOrderId());
            assertEquals(purchaseToken, orderOptional.get().getPurchaseToken());
        }

        @Test
        @DisplayName("주문 내역이 없는 경우 빈 옵셔널 객체를 반환 한다.")
        void getOrderBySocialOrderIdWhenNotExistsOrder() {
            // arrange
            String socialOrderId = CommonUtil.randomId();

            // act
            Optional<Order> orderOptional = orderPersistencePort.getOrderBySocialOrderIdPort(socialOrderId);

            // assert
            assertTrue(orderOptional.isEmpty());
        }
    }
}