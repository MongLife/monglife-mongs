package com.monglife.mongs.adapter.out.member.persistence.service;

import com.monglife.module.common.jpa.config.HibernateAutoConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.AdapterOutMemberPersistenceConfig;
import com.monglife.mongs.adapter.out.member.persistence.config.MemberDataSourceConfig;
import com.monglife.mongs.adapter.out.member.persistence.repository.OrderRepository;
import com.monglife.mongs.application.member.port.out.OrderPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AdapterOutMemberPersistenceConfig.class, MemberDataSourceConfig.class, HibernateAutoConfig.class })
class OrderPersistenceServiceTest {

    private final OrderPersistencePort orderPersistencePort;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderPersistenceServiceTest(OrderPersistencePort orderPersistencePort, OrderRepository orderRepository) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderRepository = orderRepository;
    }

    @Nested
    @DisplayName("주문 등록 단위 테스트")
    class CreateOrderPort {

        @Test
        @DisplayName("주문을 등록 한다.")
        void createOrder() {

        }
    }

    @Nested
    @DisplayName("주문 수정 단위 테스트")
    class SaveOrderPort {

        @Test
        @DisplayName("주문을 수정 한다.")
        void saveOrder() {

        }
    }

    @Nested
    @DisplayName("소비된 주문 목록 조회")
    class GetConsumedOrdersPort {

        @Test
        @DisplayName("소비된 주문 목록을 조회 한다.")
        void getConsumedOrders() {

        }
    }

    @Nested
    @DisplayName("인앱 주문 ID 기준 주문 조회")
    class GetOrderBySocialOrderIdPort {
        @Test
        @DisplayName("인앱 주문 ID 기준으로 주문을 조회 한다.")
        void getOrderBySocialOrderId() {

        }
    }
}