package com.monglife.mongs.application.mong.port.in;

import com.monglife.core.vo.page.PageResult;
import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.*;

import java.util.List;

public interface InteractionUseCase {

    /**
     * 음식 목록 조회
     */
    List<Food> getFoodsUseCase(GetFoodsCommand command);

    /**
     * 간식 목록 조회
     */
    List<Snack> getSnacksUseCase(GetSnacksCommand command);

    /**
     * 음식 섭취
     */
    Mong feedFoodUseCase(FeedFoodCommand command);

    /**
     * 간식 섭취
     */
    Mong feedSnackUseCase(FeedSnackCommand command);

    /**
     * 인벤 아이템 목록 조회
     */
    PageResult<Inventory> getInventoriesUseCase(GetInventoriesCommand command);

    /**
     * 인벤 소비성 아이템 사용
     */
    Mong useInventoryUseCase(UseInventoryCommand command);

    /**
     * 랜덤 뽑기 티켓 구매
     */
    Mong buyRandomDrawTicketUseCase(BuyRandomDrawTicketCommand command);

    /**
     * 랜덤 뽑기
     */
    RandomDraw randomDrawUseCase(RandomDrawCommand command);
}
