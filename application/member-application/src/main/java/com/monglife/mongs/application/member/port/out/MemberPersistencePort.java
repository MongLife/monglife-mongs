package com.monglife.mongs.application.member.port.out;

import com.monglife.mongs.application.member.port.out.vo.*;
import com.monglife.mongs.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface MemberPersistencePort {

    CollectionMap createCollectionMapPort(CreateCollectionMapVo createCollectionMapVo);

    CollectionMong createCollectionMongPort(CreateCollectionMongVo createCollectionMongVo);

    Boolean isExistsCollectionMap(Long accountId, String mapTypeCode);

    Boolean isExistsCollectionMong(Long accountId, String mongTypeCode);

    List<CollectionMap> getCollectionMapsPort(Long accountId);

    List<CollectionMong> getCollectionMongsPort(Long accountId);

    Feedback createFeedback(CreateFeedbackVo createFeedbackVo);

    Player createPlayerPort(CreatePlayerVo createPlayerVo);

    Boolean isExistsPlayerPort(Long accountId);

    Optional<Player> getPlayerPort(Long accountId);

    void savePlayerPort(Player player);

    List<String> getProductIdsPort();

    Optional<ExchangeStarPointProduct> getExchangeStarPointProductPort(String productId);

    Order createOrderPort(CreateOrderVo createOrderVo);

    void saveOrderPort(Order order);

    List<Order> getConsumedOrdersPort(Long accountId);

    Optional<Order> getOrderPort(Long orderId);

    Optional<Order> getOrderBySocialOrderIdPort(String socialOrderId);
}
