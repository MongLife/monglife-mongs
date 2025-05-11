package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.model.*;

import java.util.List;

public interface InteractionUseCase {

    List<Food> getFoodsUseCase(GetFoodsCommand command);

    List<Snack> getSnacksUseCase(GetSnacksCommand command);

    Mong feedFoodUseCase(FeedFoodCommand command);

    Mong feedSnackUseCase(FeedSnackCommand command);

    List<InventoryItem> getInventoryItemsUseCase(GetInventoryItemsCommand command);

    List<InventoryItem> useInventoryItemUseCase(UseInventoryItemCommand command);

    RandomDrawItem randomDrawUseCase(RandomDrawCommand command);
}
